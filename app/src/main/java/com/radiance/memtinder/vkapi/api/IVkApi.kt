package com.radiance.memtinder.vkapi.api

import android.app.Activity
import com.radiance.memtinder.vkapi.group.VkGroup

interface IVkApi {
    fun authorization(activity: Activity)
    fun isAuthorized(): Boolean

    fun requestGroups()
    fun requestMemes(groups: List<VkGroup>, count: Int, startFrom: String)
    fun requestRecommendedMemes(count: Int, startFrom: String)

    fun addGroupListener(groupListener: GroupListener)
    fun removeGroupListener(groupListener: GroupListener)
    fun addMemesListener(memesListener: MemesListener)
    fun removeMemesListener(memesListener: MemesListener)
    fun addRecommendedMemesListener(memesListener: RecommendedMemesListener)
    fun removeRecommendedMemesListener(memesListener: RecommendedMemesListener)
    fun addAuthorizationListener(listener: AuthorizationListener)
    fun removeAuthorizationListener(listener: AuthorizationListener)

    interface GroupListener {
        fun receiveGroup(answer: GroupAnswer)
    }

    interface MemesListener {
        fun receiveMemes(answer: MemesAnswer)
    }

    interface RecommendedMemesListener {
        fun receiveMemes(answer: MemesAnswer)
    }

    interface AuthorizationListener {
        fun isAuthorized(boolean: Boolean)
    }
}