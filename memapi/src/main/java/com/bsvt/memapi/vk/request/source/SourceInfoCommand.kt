package com.bsvt.memapi.vk.request.source

import com.bsvt.memapi.vk.request.getSourceList
import com.bsvt.memapi.vk.request.items
import com.bsvt.memapi.vk.request.responce
import com.radiance.core.Id
import com.radiance.core.Source
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class SourceInfoCommand(private val id: Id) : ApiCommand<Source>() {
    override fun onExecute(manager: VKApiManager): Source {
        val call = VKMethodCall.Builder()
            .method("groups.getById")
            .args("group_id", id)
            .version(manager.config.version)
            .build()

        return manager.execute(
            call,
            Parser()
        )
    }

    class Parser: VKApiResponseParser<Source> {
        override fun parse(response: String?): Source {
            response?.let {
                try {
                    val responseJson = JSONObject(it)
                    val responseObject = responseJson.responce()
                    val sourceArray = responseObject.items()

                    val sourceList = sourceArray.getSourceList()
                    if (sourceList.size == 1) {
                        return sourceList[0]
                    }
                } catch (e: JSONException) {

                }
            }

            return Source.empty
        }
    }
}