package com.radiance.data.vkapi.vk

import com.vk.api.sdk.VKApiCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class CallbackChannel<T>(private val channel: Channel<T>, dispatcher: CoroutineDispatcher): VKApiCallback<T> {
    private val scope = CoroutineScope(dispatcher)

    override fun fail(error: Exception) {

    }

    override fun success(result: T) {
        scope.launch {
            channel.send(result)
        }
    }
}