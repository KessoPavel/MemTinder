package com.radiance.sourcestorage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radiance.sourcestorage.db.entity.SourceEntity
import com.radiance.sourcestorage.db.entity.SourceStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {
    @Query("SELECT * FROM SourceEntity")
    fun getAll(): Flow<List<SourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg storage: SourceEntity)

    @Query ("SELECT * FROM SourceEntity WHERE status == :status")
    fun getSources(status: SourceStatus): Flow<List<SourceEntity>>
}