package com.radiance.memtinder.memProvider.news

import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.memes.VkMemes

interface IMemProvider {
    fun load()
    fun isLoaded(): Boolean

    fun addUpdateListener(listener: UpdateGroupListener)
    fun removeUpdateLister(listener: UpdateGroupListener)

    fun addMemListener(listener: MemListener)
    fun removeMemListener(listener: MemListener)

    fun getGroups(): List<VkGroup>
    fun getEnabledGroup(): List<VkGroup>
    fun enableMemFromGroup(group: VkGroup, enable: Boolean)
    fun clearEnabled()
    fun enabledAll()

    fun requestMemes(count: Int)
    fun cleatStartFrom()
    fun recommendedNews()
    interface MemListener {
        fun receiveMemes(memes: List<VkMemes>)
    }

    interface UpdateGroupListener {
        fun groupLoaded()
    }
}