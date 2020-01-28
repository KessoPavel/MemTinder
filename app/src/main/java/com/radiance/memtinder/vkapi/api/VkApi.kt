package com.radiance.memtinder.vkapi.api

import android.app.Activity
import android.content.SharedPreferences
import com.radiance.memtinder.vkapi.group.VkGroup
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlin.concurrent.fixedRateTimer

object VkApi : IVkApi {

    val callback = object : VKAuthCallback {
        override fun onLogin(token: VKAccessToken) {
            for (listener in authorizationListener) {
                listener.isAuthorized(true)
            }
            isAuthorized = true
        }

        override fun onLoginFailed(errorCode: Int) {
            for (listener in authorizationListener) {
                listener.isAuthorized(false)
            }
            isAuthorized = false
        }
    }

    private val groupListenerList = ArrayList<IVkApi.GroupListener>()
    private val memesListenerList = ArrayList<IVkApi.MemesListener>()
    private val authorizationListener = ArrayList<IVkApi.AuthorizationListener>()

    private val recommendedMemesListenerList = ArrayList<IVkApi.RecommendedMemesListener>()

    private var isAuthorized: Boolean = false

    override fun authorization(activity: Activity) {
        activity.applicationContext?.let {
            VK.initialize(it)
        }

        VK.addTokenExpiredHandler(tokenTracker)

        if (!VK.isLoggedIn()) {
            VK.login(
                activity,
                arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.GROUPS, VKScope.FRIENDS)
            )
        } else {
            for (listener in authorizationListener) {
                listener.isAuthorized(true)
            }
            isAuthorized = true
        }
    }

    override fun isAuthorized(): Boolean {
        return isAuthorized
    }

    override fun requestGroups() {
        VK.execute(GroupRequest(groupListenerList))
    }

    override fun requestMemes(groups: List<VkGroup>, count: Int, startFrom: String) {
        VK.execute(NewsfeedRequest(memesListenerList, groups, count, startFrom))
    }

    override fun requestRecommendedMemes(count: Int, startFrom: String) {
        VK.execute(RecommendedRequest(recommendedMemesListenerList, count, startFrom))
    }

    override fun addGroupListener(groupListener: IVkApi.GroupListener) {
        if (!groupListenerList.contains(groupListener)) {
            groupListenerList.add(groupListener)
        }
    }

    override fun removeGroupListener(groupListener: IVkApi.GroupListener) {
        if (groupListenerList.contains(groupListener)) {
            groupListenerList.remove(groupListener)
        }
    }

    override fun addMemesListener(memesListener: IVkApi.MemesListener) {
        if (!memesListenerList.contains(memesListener)) {
            memesListenerList.add(memesListener)
        }
    }

    override fun removeMemesListener(memesListener: IVkApi.MemesListener) {
        if (memesListenerList.contains(memesListener)) {
            memesListenerList.remove(memesListener)
        }
    }

    override fun addRecommendedMemesListener(memesListener: IVkApi.RecommendedMemesListener) {
        if (!recommendedMemesListenerList.contains(memesListener)) {
            recommendedMemesListenerList.add(memesListener)
        }
    }

    override fun removeRecommendedMemesListener(memesListener: IVkApi.RecommendedMemesListener) {
        if (recommendedMemesListenerList.contains(memesListener)) {
            recommendedMemesListenerList.remove(memesListener)
        }
    }

    override fun addAuthorizationListener(listener: IVkApi.AuthorizationListener) {
        if (!authorizationListener.contains(listener)) {
            authorizationListener.add(listener)
        }
    }

    override fun removeAuthorizationListener(listener: IVkApi.AuthorizationListener) {
        if (authorizationListener.contains(listener)) {
            authorizationListener.remove(listener)
        }
    }

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
        }
    }
}