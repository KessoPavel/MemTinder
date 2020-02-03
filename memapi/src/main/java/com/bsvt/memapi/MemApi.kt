package com.bsvt.memapi

import android.app.Activity
import com.radiance.core.Id
import com.radiance.core.Mem
import com.radiance.core.Source

interface MemApi {
    fun toRegister(activity: Activity)
    fun isRegistered(): Boolean

    fun requestMem(count: Int, fromStart: Boolean, sourceType: SourceType)
    fun requestSources()
    fun requestSource(id: Id)
    fun enabledSource(source: Source, enabled: Boolean)

    fun addStateListener(listener: MemApiListener)
    fun removeStateListener(listener: MemApiListener)

    interface MemApiListener {
        fun sourcesUpdate()
        fun enabledSourcesUpdate()
        fun memes(sourceType: SourceType, memes: List<Mem>)
    }
}