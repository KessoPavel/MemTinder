package com.radiance.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.radiance.storage.room.entity.EnabledId
import com.radiance.storage.room.entity.RecommendedSource

@Dao
interface EnabledDao {
    @Query("SELECT * FROM EnabledId")
    fun getAll(): List<EnabledId>

    @Insert
    fun insertAll(vararg id: EnabledId)

    @Query("SELECT * FROM EnabledId WHERE vkId=(:ids)")
    fun getByIds(ids: Long): List<EnabledId>

    @Delete
    fun delete(id: EnabledId)
}