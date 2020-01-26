package com.radiance.memtinder.memProvider

import android.content.SharedPreferences
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.memes.VkMemes

interface IMemProvider {
    fun load()

    fun addUpdateListener(listener: UpdateGroupListener)
    fun removeUpdateLister(listener: UpdateGroupListener)

    fun addMemListener(listener: MemListener)
    fun removeMemListener(listener: MemListener)

    fun getGroups(): List<VkGroup>
    fun enableMemFromGroup(group: VkGroup, enable: Boolean)

    fun requestMemes(count: Int)

    interface MemListener {
        fun receiveMemes(memes: List<VkMemes>)
    }

    interface UpdateGroupListener {
        fun groupLoaded()
    }
}