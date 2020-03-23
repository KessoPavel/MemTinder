package com.radiance.domain.repositories

import android.app.Activity
import com.radiance.domain.entity.Id
import com.radiance.domain.entity.Mem
import com.radiance.domain.entity.Source
import kotlinx.coroutines.flow.Flow

interface MemRepository {
    suspend fun toRegister(activity: Activity): Boolean
    fun isRegistered(): Boolean

    fun startMemFlow(step: Int, fromStart: Boolean) : Flow<Mem>
    fun startRecommendedMemFlow(step: Int, fromStart: Boolean) : Flow<Mem>

    fun requestSources(): Flow<List<Source>>
    fun requestSource(id: Id): Flow<Source>
    suspend fun enabledSource(source: Source, enabled: Boolean)
}