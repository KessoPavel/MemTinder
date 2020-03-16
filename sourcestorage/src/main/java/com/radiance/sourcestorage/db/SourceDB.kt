package com.radiance.sourcestorage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.radiance.sourcestorage.db.converters.AvatarConverter
import com.radiance.sourcestorage.db.converters.StatusConverter
import com.radiance.sourcestorage.db.dao.SourceDao
import com.radiance.sourcestorage.db.entity.SourceEntity

@Database(entities = [SourceEntity::class], version = 1)
@TypeConverters(AvatarConverter::class, StatusConverter::class)
abstract class SourceDB: RoomDatabase() {
    abstract fun sourceDao(): SourceDao
}