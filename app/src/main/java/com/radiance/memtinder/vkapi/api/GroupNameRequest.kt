package com.radiance.memtinder.vkapi.api

import com.radiance.memtinder.ui.groupAdapter.GroupAdapter
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.image.Resolution
import com.radiance.memtinder.vkapi.image.VkImage
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject
import java.lang.Exception

class GroupNameRequest(
    private val groupId: VkId,
    private val listeners: ArrayList<IVkApi.GroupInfoListener>
)
    :ApiCommand<VkGroup>() {
    override fun onExecute(manager: VKApiManager): VkGroup {
        val call = VKMethodCall.Builder()
            .method("groups.getById")
            .args("group_id", groupId.getPositiveId())
            .version(manager.config.version)
            .build()

        return manager.execute(
            call,
            GroupNameRequestParser(listeners)
        )
    }

    private class GroupNameRequestParser(private val listeners: ArrayList<IVkApi.GroupInfoListener>)
        : VKApiResponseParser<VkGroup> {
        override fun parse(response: String?): VkGroup {
            try {
                val o = JSONObject(response)

                val responseArray = o.getJSONArray("response")
                if (responseArray.length() == 1) {
                    val group = responseArray.getJSONObject(0)

                    val name = group.getString("name")
                    val photo_50 = group.getString("photo_50")
                    val photo_100 = group.getString("photo_100")
                    val photo_200 = group.getString("photo_200")
                    val id = group.getString("id")

                    val idObj = VkId(-id.toLong())
                    val image = VkImage.Builder().addImage(Resolution(50,50), photo_50)
                        .addImage(Resolution(100,100), photo_100)
                        .addImage(Resolution(200,200),photo_200)
                        .build()

                    val answer = VkGroup(name, image, idObj)

                    for (listener in listeners) {
                        listener.receiveGroup(answer)
                    }
                }

            } catch (e: Exception) {

            }

            return VkGroup("", VkImage.Builder().build(), VkId(0))
        }
    }
}