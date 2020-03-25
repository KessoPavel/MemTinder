package com.radiance.data.vkapi.vk.request.recommended

import com.radiance.data.vkapi.vk.request.newsfeed.MemAnswer
import com.radiance.data.vkapi.vk.request.newsfeed.NewsfeedCommand
import com.vk.api.sdk.*
import com.vk.api.sdk.internal.ApiCommand

class RecommendedCommand(
    private val request: RecommendedRequest
) : ApiCommand<MemAnswer>() {

    fun execute(callback: VKApiCallback<MemAnswer>) {
        VK.execute(this, callback)
    }

    override fun onExecute(manager: VKApiManager): MemAnswer {
        val callBuilder = VKMethodCall.Builder()
            .method("newsfeed.getRecommended")
            .args("count", request.count)
            .version(manager.config.version)

        if (request.startFrom != "") {
            callBuilder.args("start_from", request.startFrom)
        }


        return manager.execute(
            callBuilder.build(),
            NewsfeedCommand.Parser()
        )
    }
}