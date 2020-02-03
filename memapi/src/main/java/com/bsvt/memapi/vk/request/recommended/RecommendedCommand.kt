package com.bsvt.memapi.vk.request.recommended

import com.bsvt.memapi.vk.request.getMemes
import com.bsvt.memapi.vk.request.items
import com.bsvt.memapi.vk.request.newsfeed.MemAnswer
import com.bsvt.memapi.vk.request.newsfeed.NewsfeedCommand
import com.bsvt.memapi.vk.request.nextFrom
import com.bsvt.memapi.vk.request.responce
import com.radiance.core.Mem
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class RecommendedCommand(
    private val request: RecommendedRequest
) : ApiCommand<MemAnswer>() {

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