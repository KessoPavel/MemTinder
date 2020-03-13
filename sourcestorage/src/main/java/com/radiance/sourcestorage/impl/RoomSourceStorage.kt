package com.radiance.sourcestorage.impl

import android.content.Context
import androidx.room.Room
import com.radiance.sourcestorage.contract.SourceStorage
import com.radiance.sourcestorage.db.SourceDB
import com.radiance.sourcestorage.db.entity.SourceEntity
import com.radiance.sourcestorage.db.entity.SourceStatus
import kotlinx.coroutines.flow.Flow

class RoomSourceStorage(context: Context): SourceStorage {
    private val db: SourceDB = Room.databaseBuilder(context, SourceDB::class.java, "source_storage").build()

    override suspend fun updateAllSource(vararg storage: SourceEntity) {
        db.sourceDao().insertAll(*storage)
    }

    override fun getAllSource(): Flow<List<SourceEntity>> {
        return db.sourceDao().getAll()
    }

    override fun getEnabledSource(): Flow<List<SourceEntity>> {
        return db.sourceDao().getSources(SourceStatus.Enabled)
    }
}