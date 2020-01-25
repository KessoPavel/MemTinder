package com.radiance.memtinder.vkapi.api

import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.image.Resolution
import com.radiance.memtinder.vkapi.image.VkImage
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

class GroupRequest(private val listeners: ArrayList<IVkApi.GroupListener>): ApiCommand<GroupAnswer>() {
    override fun onExecute(manager: VKApiManager): GroupAnswer {
        val call = VKMethodCall.Builder()
            .method("groups.get")
            .args("extended", true)
            .version(manager.config.version)
            .build()


        return manager.execute(
            call,
            GroupRequestParser(listeners)
        )
    }

    private class GroupRequestParser(private val listeners: ArrayList<IVkApi.GroupListener>): VKApiResponseParser<GroupAnswer> {
        override fun parse(response: String?): GroupAnswer {
            val answer = ArrayList<VkGroup>()

            val o = JSONObject(response)
            val responseObject = o.getJSONObject("response")

            val items = responseObject.getJSONArray("items")

            for (i in 0 until items.length()) {
                val group = items.getJSONObject(i)

                val avatar = VkImage.Builder()
                    .addImage(Resolution(50, 50), group.getString("photo_50"))
                    .addImage(Resolution(100, 100), group.getString("photo_100"))
                    .addImage(Resolution(200, 200), group.getString("photo_200"))
                    .build()
                val id = VkId(group.getLong("id"))

                answer.add(VkGroup(group.getString("name"), avatar, id))
            }
            val groupAnswer = GroupAnswer(answer)

            listeners.forEach {
                it.receiveGroup(groupAnswer)
            }

            return groupAnswer
        }
    }
}