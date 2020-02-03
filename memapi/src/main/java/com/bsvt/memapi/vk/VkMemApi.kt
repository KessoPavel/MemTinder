package com.bsvt.memapi.vk

import android.app.Activity
import com.bsvt.memapi.MemApi
import com.bsvt.memapi.SourceType
import com.radiance.core.Id
import com.radiance.core.Mem
import com.radiance.core.Source
import com.radiance.storage.SourceStorage

class VkMemApi(private val storage: SourceStorage) : MemApi {
    private val listenerList = ArrayList<MemApi.MemApiListener>()

    override fun toRegister(activity: Activity) {
    }

    override fun isRegistered(): Boolean {
        return false
    }

    override fun requestMem(count: Int, fromStart: Boolean, sourceType: SourceType) {
    }

    override fun requestSources() {
    }

    override fun requestSource(id: Id) {
    }

    override fun enabledSource(source: Source, enabled: Boolean) {
        storage.enableSource(source.id, enabled)
        notifySourceChange()
    }

    override fun addStateListener(listener: MemApi.MemApiListener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener)
        }
    }

    override fun removeStateListener(listener: MemApi.MemApiListener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener)
        }
    }

    private fun notifySourceChange() {
        listenerList.forEach {
            it.sourcesUpdate()
        }
    }

    private fun notifyEnabledSourceChange() {
        listenerList.forEach{
            it.enabledSourcesUpdate()
        }
    }

    private fun sendMemes(sourceType: SourceType, memes: List<Mem>) {
        listenerList.forEach {
            it.memes(sourceType, memes)
        }
    }
}