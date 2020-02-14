package com.radiance.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.radiance.core.Source
import com.radiance.storage.room.entity.RecommendedSource

@Dao
interface SourceDao {
    @Query("SELECT * FROM RecommendedSource")
    fun getAll(): List<RecommendedSource>

    @Query("SELECT * FROM RecommendedSource WHERE vkId=(:ids)")
    fun getByIds(ids: Long): List<RecommendedSource>

    @Insert
    fun insertAll(vararg sources: RecommendedSource)

    @Delete
    fun delete(source: RecommendedSource)
}