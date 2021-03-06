package com.radiance.data.vkapi.vk.request.source

import com.bsvt.memapi.vk.request.*
import com.radiance.domain.entity.Source
import com.vk.api.sdk.*
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class SourcesCommand : ApiCommand<SourceAnswer>() {

    fun execute(callback: VKApiCallback<SourceAnswer>) {
        VK.execute(this, callback)
    }

    override fun onExecute(manager: VKApiManager): SourceAnswer {
        val call = VKMethodCall.Builder()
            .method("groups.get")
            .args("extended", true)
            .version(manager.config.version)
            .build()

        return manager.execute(
            call,
            Parser()
        )
    }

    class Parser: VKApiResponseParser<SourceAnswer> {

        override fun parse(response: String?): SourceAnswer {
            val answer = ArrayList<Source>()

            response?.let {
                try {
                    val responseJson = JSONObject(it)
                    val responseObject = responseJson.responce()
                    val sourceArray = responseObject.items()

                    val sourceList = sourceArray.getSourceList()
                    answer += sourceList
                } catch (e: JSONException) {

                }
            }

            return SourceAnswer(answer)
        }
    }
}