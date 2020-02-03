package com.bsvt.memapi.vk.request.source

import com.bsvt.memapi.vk.request.*
import com.radiance.core.Source
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class SourcesCommand : ApiCommand<SourceAnswer>() {

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