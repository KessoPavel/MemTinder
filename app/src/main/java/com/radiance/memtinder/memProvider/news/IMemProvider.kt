package com.radiance.memtinder.memProvider.news

import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.memes.VkMemes

interface IMemProvider {
    fun load(source: Source)
    fun isLoaded(): Boolean

    fun setSource(source: Source)

    fun addUpdateListener(listener: UpdateGroupListener)
    fun removeUpdateLister(listener: UpdateGroupListener)

    fun addMemListener(listener: MemListener)
    fun removeMemListener(listener: MemListener)

    fun getGroups(): List<VkGroup>
    fun getEnabledGroup(): List<VkGroup>
    fun getRecommendedGroup(): List<VkGroup>
    fun enableMemFromGroup(group: VkGroup, enable: Boolean)
    fun clearEnabled()
    fun enabledAll()

    fun requestMemes(count: Int, update: Boolean = false)

    interface MemListener {
        fun receiveNews(memes: List<VkMemes>)
        fun receiveRecommended(memes: List<VkMemes>)
    }

    interface UpdateGroupListener {
        fun groupLoaded()
        fun recommendedGroupUpdated()
    }
}