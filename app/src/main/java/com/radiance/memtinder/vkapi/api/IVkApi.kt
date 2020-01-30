package com.radiance.memtinder.vkapi.api

import android.app.Activity
import android.content.SharedPreferences
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId

interface IVkApi {
    fun authorization(activity: Activity)
    fun isAuthorized(): Boolean

    fun requestGroups()
    fun requestMemes(groups: List<VkGroup>, count: Int, startFrom: String)
    fun requestRecommendedMemes(count: Int, startFrom: String)
    fun requestGroup(id: VkId)

    fun addGroupListener(groupListener: GroupListener)
    fun removeGroupListener(groupListener: GroupListener)
    fun addMemesListener(memesListener: MemesListener)
    fun removeMemesListener(memesListener: MemesListener)
    fun addRecommendedMemesListener(memesListener: RecommendedMemesListener)
    fun removeRecommendedMemesListener(memesListener: RecommendedMemesListener)
    fun addAuthorizationListener(listener: AuthorizationListener)
    fun removeAuthorizationListener(listener: AuthorizationListener)

    fun addNewGroupListener(listener: GroupInfoListener)
    fun removeNewGroupListener(listener: GroupInfoListener)

    interface GroupListener {
        fun receiveGroup(answer: GroupAnswer)
    }

    interface MemesListener {
        fun receiveNews(answer: MemesAnswer)
    }

    interface RecommendedMemesListener {
        fun receiveRecommended(answer: MemesAnswer)
    }

    interface AuthorizationListener {
        fun isAuthorized(boolean: Boolean)
    }

    interface GroupInfoListener {
        fun receiveGroup(group: VkGroup)
    }
}