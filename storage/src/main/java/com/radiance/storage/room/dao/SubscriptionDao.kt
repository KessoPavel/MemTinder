package com.radiance.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.radiance.storage.room.entity.SubscriptionSource

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM SubscriptionSource")
    fun getAll(): List<SubscriptionSource>

    @Query("SELECT * FROM SubscriptionSource WHERE vkId=(:ids)")
    fun getByIds(ids: Long): List<SubscriptionSource>

    @Insert
    fun insertAll(vararg sources: SubscriptionSource)

    @Delete
    fun delete(source: SubscriptionSource)

    @Query("DELETE FROM SubscriptionSource")
    fun deleteAll()
}