package com.bsvt.memapi.contract

import android.app.Activity
import com.bsvt.memapi.MemApi
import com.bsvt.memapi.SourceType
import com.radiance.core.Id
import com.radiance.core.Mem
import com.radiance.core.Source
import com.radiance.sourcestorage.db.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

interface MemApi {
    suspend fun toRegister(activity: Activity): Boolean
    fun isRegistered(): Boolean

    fun startMemFlow(step: Int, fromStart: Boolean) : Flow<Mem>
    fun startRecommendedMemFlow(step: Int, fromStart: Boolean) : Flow<Mem>

    fun requestSources(): Flow<List<SourceEntity>>
    suspend fun requestSource(id: Id): Flow<Source>
    suspend fun enabledSource(source: Source, enabled: Boolean)
}