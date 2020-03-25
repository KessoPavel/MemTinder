package com.radiance.data.vkapi

import android.app.Activity
import android.content.Intent
import com.radiance.data.vkapi.vk.AuthorizationActivity
import com.radiance.data.vkapi.vk.CallbackChannel
import com.radiance.data.vkapi.vk.request.newsfeed.MemAnswer
import com.radiance.data.vkapi.vk.request.newsfeed.NewsfeedCommand
import com.radiance.data.vkapi.vk.request.newsfeed.NewsfeedRequest
import com.radiance.data.vkapi.vk.request.recommended.RecommendedCommand
import com.radiance.data.vkapi.vk.request.recommended.RecommendedRequest
import com.radiance.data.vkapi.vk.request.source.SourceAnswer
import com.radiance.data.vkapi.vk.request.source.SourceInfoCommand
import com.radiance.data.vkapi.vk.request.source.SourcesCommand
import com.radiance.domain.entity.Id
import com.radiance.domain.entity.Source
import com.vk.api.sdk.VK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel

object VkApi {
    val newsChannel = Channel<MemAnswer>()
    val recommendedChannel = Channel<MemAnswer>()
    val sourceChannel = Channel<SourceAnswer>()
    val sourceInfoChannel = Channel<Source>()

    private val newsCallback = CallbackChannel(newsChannel, Dispatchers.IO)
    private val recommendedCallback = CallbackChannel(recommendedChannel, Dispatchers.IO)
    private val sourceCallback = CallbackChannel(sourceChannel, Dispatchers.IO)
    private val sourceInfoCallback = CallbackChannel(sourceInfoChannel, Dispatchers.IO)

    fun authorization(
        activity: Activity
    ) {
        val intent = Intent(activity, AuthorizationActivity::class.java)
        activity.startActivity(intent)
    }

    fun isAuthorize(): Boolean {
        return VK.isLoggedIn()
    }

    fun requestNewsfeed(request: NewsfeedRequest) {
        val newsfeedCommand = NewsfeedCommand(request)
        newsfeedCommand.execute(newsCallback)
    }

    fun requestRecommended(request: RecommendedRequest) {
        val recommendedCommand = RecommendedCommand(request)
        recommendedCommand.execute(recommendedCallback)
    }

    fun requestSources() {
        val sourceCommand = SourcesCommand()
        sourceCommand.execute(sourceCallback)
    }

    fun requestSource(id: Id) {
        val sourceCommand = SourceInfoCommand(id)
        sourceCommand.execute(sourceInfoCallback)
    }
}