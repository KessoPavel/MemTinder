package com.radiance.memtinder.vkapi.api

import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.image.Resolution
import com.radiance.memtinder.vkapi.image.VkImage
import com.radiance.memtinder.vkapi.memes.VkMemes
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class RecommendedRequest(
    private val listeners: ArrayList<IVkApi.RecommendedMemesListener>,
    private val count: Int,
    private val startFrom: String
) : ApiCommand<MemesAnswer>() {
    override fun onExecute(manager: VKApiManager): MemesAnswer {
        val callBuilder = VKMethodCall.Builder()
            .method("newsfeed.getRecommended")
            .args("count", count)
            .version(manager.config.version)

        if (startFrom != "") {
            callBuilder.args("start_from", startFrom)
        }


        return manager.execute(
            callBuilder.build(),
            GroupRequestParser(listeners)
        )
    }

    private class GroupRequestParser(private val listeners: ArrayList<IVkApi.RecommendedMemesListener>) :
        VKApiResponseParser<MemesAnswer> {
        override fun parse(response: String?): MemesAnswer {
            val answer = ArrayList<VkMemes>()

            try {

                val o = JSONObject(response)

                val responseObject = o.getJSONObject("response")
                val items = responseObject.getJSONArray("items")
                val nextfrom = responseObject.getString("next_from")

                for (i in 0 until items.length()) {

                    try {
                        val item = items.getJSONObject(i)
                        val text = item.getString("text")
                        val source_id = item.getString("source_id")

                        val photos = ArrayList<VkImage>()
                        val attachment = item.getJSONArray("attachments")

                        for (j in 0 until attachment.length()) {
                            val att = attachment.getJSONObject(j)

                            if (att.getString("type") == "photo") {
                                val attachmentPhoto = att.getJSONObject("photo")
                                val sizes = attachmentPhoto.getJSONArray("sizes")

                                val imageBuilder = VkImage.Builder()

                                for (k in 0 until sizes.length()) {
                                    val photo = sizes.getJSONObject(k)
                                    val resolution =
                                        Resolution(photo.getInt("width"), photo.getInt("height"))
                                    val url = photo.getString("url")

                                    imageBuilder.addImage(resolution, url)
                                }

                                val image = imageBuilder.build()

                                photos.add(image)
                            }
                        }

                        val sourceId = VkId(source_id)

                        if (photos.isNotEmpty()) {
                            answer.add(VkMemes(text, photos, sourceId))
                        }
                    } catch (e: JSONException) {

                    }
                }

                val memesAnswer = MemesAnswer(answer, nextFrom = nextfrom)

                listeners.forEach {
                    it.receiveMemes(memesAnswer)
                }

                return memesAnswer

            } catch (e: JSONException) {
                val memesAnswer = MemesAnswer(answer, nextFrom = "")

                listeners.forEach {
                    it.receiveMemes(memesAnswer)
                }

                return memesAnswer
            }
        }
    }
}