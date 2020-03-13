package com.radiance.sourcestorage.contract

import com.radiance.sourcestorage.db.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

interface SourceStorage {
    suspend fun updateAllSource(vararg storage: SourceEntity)
    fun getAllSource(): Flow<List<SourceEntity>>
    fun getEnabledSource(): Flow<List<SourceEntity>>
}