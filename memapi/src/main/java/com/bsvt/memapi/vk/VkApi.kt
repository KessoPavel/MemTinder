package com.bsvt.memapi.vk

import android.app.Activity
import com.bsvt.memapi.MemApi
import com.bsvt.memapi.vk.request.newsfeed.MemAnswer
import com.bsvt.memapi.vk.request.newsfeed.NewsfeedCommand
import com.bsvt.memapi.vk.request.newsfeed.NewsfeedRequest
import com.bsvt.memapi.vk.request.recommended.RecommendedCommand
import com.bsvt.memapi.vk.request.recommended.RecommendedRequest
import com.bsvt.memapi.vk.request.source.SourceAnswer
import com.bsvt.memapi.vk.request.source.SourceInfoCommand
import com.bsvt.memapi.vk.request.source.SourcesCommand
import com.radiance.core.Id
import com.radiance.core.Source
import com.vk.api.sdk.VKApiCallback

object VkApi {
    fun authorization(
        activity: Activity,
        listener: MemApi.AuthorizationListener
    ) {
        //todo show authorization activity
    }

    fun isAuthorize(): Boolean {
        return false
    }

    fun requestSources(callback: VKApiCallback<SourceAnswer>) {
        val sourceCommand = SourcesCommand()
        sourceCommand.execute(callback)
    }

    fun requestNewsfeed(request: NewsfeedRequest, callback: VKApiCallback<MemAnswer>) {
        val newsfeedCommand = NewsfeedCommand(request)
        newsfeedCommand.execute(callback)
    }

    fun requestRecommended(request: RecommendedRequest, callback: VKApiCallback<MemAnswer>) {
        val recommendedCommand = RecommendedCommand(request)
        recommendedCommand.execute(callback)
    }

    fun requestSource(id: Id, callback: VKApiCallback<Source>) {
        val sourceCommand = SourceInfoCommand(id)
        sourceCommand.execute(callback)
    }
}