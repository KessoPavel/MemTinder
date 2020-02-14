package com.radiance.storage.room.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.radiance.storage.room.dao.EnabledDao
import com.radiance.storage.room.dao.SourceDao
import com.radiance.storage.room.dao.SubscriptionDao
import com.radiance.storage.room.entity.EnabledId
import com.radiance.storage.room.entity.RecommendedSource
import com.radiance.storage.room.entity.SubscriptionSource

@Database(entities = arrayOf(EnabledId::class, RecommendedSource::class, SubscriptionSource::class), version = 2)
abstract class SourceDataBase: RoomDatabase() {
    abstract fun recommendedDao(): SourceDao
    abstract fun enabledDao(): EnabledDao
    abstract fun subscriptionDao(): SubscriptionDao
}