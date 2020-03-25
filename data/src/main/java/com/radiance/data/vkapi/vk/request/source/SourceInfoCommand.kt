package com.radiance.data.vkapi.vk.request.source

import com.bsvt.memapi.vk.request.getSourceList
import com.bsvt.memapi.vk.request.responseArray
import com.radiance.domain.entity.Id
import com.radiance.domain.entity.Source
import com.vk.api.sdk.*
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class SourceInfoCommand(private val id: Id) : ApiCommand<Source>() {

    fun execute(callback: VKApiCallback<Source>) {
        VK.execute(this, callback)
    }

    override fun onExecute(manager: VKApiManager): Source {
        val call = VKMethodCall.Builder()
            .method("groups.getById")
            .args("group_id", (-id.toLong()).toString())
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
                    val sourceList = responseJson.responseArray().getSourceList()
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