package com.radiance.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnabledId (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "vkId") val vkId: Long
)