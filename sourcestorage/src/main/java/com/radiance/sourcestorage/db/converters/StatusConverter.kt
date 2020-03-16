package com.radiance.sourcestorage.db.converters

import androidx.room.TypeConverter
import com.radiance.sourcestorage.db.entity.Image
import com.radiance.sourcestorage.db.entity.SourceStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class StatusConverter {
    @TypeConverter
    fun statusToInt(status: SourceStatus) : Int {
        return when (status) {
            SourceStatus.Enabled -> 0
            SourceStatus.Unknown -> 1
        }
    }

    @TypeConverter
    fun jsonToAvatar(status: Int): SourceStatus {
        return when (status) {
            0 -> SourceStatus.Enabled
            1 -> SourceStatus.Unknown
            else -> SourceStatus.Unknown
        }
    }
}