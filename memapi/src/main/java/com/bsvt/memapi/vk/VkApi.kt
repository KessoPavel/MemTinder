package com.bsvt.memapi.vk

import android.app.Activity
import android.content.Intent
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
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback

object VkApi {
    private val listenerList = ArrayList<MemApi.AuthorizationListener>()

    fun authorization(
        activity: Activity
    ) {
        val intent = Intent(activity, AuthorizationActivity::class.java)
        activity.startActivity(intent)
    }

    fun isAuthorize(): Boolean {
        return VK.isLoggedIn()
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

    fun addListener(listener: MemApi.AuthorizationListener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener)
        }
    }

    fun removeListener(listener: MemApi.AuthorizationListener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener)
        }
    }

    fun authorizationSuccess(success: Boolean) {
        listenerList.forEach{
            it.isAuthorize(success)
        }
    }
}