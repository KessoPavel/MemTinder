package com.radiance.data.vkapi.vk.request.newsfeed

import com.bsvt.memapi.vk.request.*
import com.radiance.domain.entity.Mem
import com.vk.api.sdk.*
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class NewsfeedCommand(private val request: NewsfeedRequest) : ApiCommand<MemAnswer>() {

    fun execute(callback: VKApiCallback<MemAnswer>) {
        VK.execute(this, callback)
    }

    override fun onExecute(manager: VKApiManager): MemAnswer {
        var sourceId = ""

        request.sources.forEach {
            sourceId += "${it.id}, "
        }

        val call = VKMethodCall.Builder()
            .method("newsfeed.get")
            .args("filters", "post")
            .args("source_ids", sourceId)
            .args("count", request.count)
            .args("start_from", request.startFrom)
            .version(manager.config.version)
            .build()

        return manager.execute(
            call,
            Parser()
        )
    }

    class Parser: VKApiResponseParser<MemAnswer> {
        override fun parse(response: String?): MemAnswer {
            val answer = ArrayList<Mem>()

            response?.let {
                try {
                    val jsonResponse = JSONObject(it)

                    val responseObject = jsonResponse.responce()
                    val nextFrom = responseObject.nextFrom()

                    val memArray = responseObject.items()
                    answer.addAll(memArray.getMemes())

                    return MemAnswer(
                        answer,
                        nextFrom
                    )
                } catch (e: JSONException) {

                }
            }

            return MemAnswer.empty
        }
    }
}