package com.radiance.sourcestorage.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SourceEntity (
    @PrimaryKey(autoGenerate = false)
    val vkId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "avatar")
    val avatar: List<Image>,
    @ColumnInfo(name = "status")
    var status: SourceStatus
)