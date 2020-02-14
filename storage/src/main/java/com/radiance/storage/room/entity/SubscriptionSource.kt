package com.radiance.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubscriptionSource(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "vkId") val vkId: Long,
    @ColumnInfo(name = "avatar50") val avatar50: String,
    @ColumnInfo(name = "avatar100") val avatar100: String,
    @ColumnInfo(name = "avatar200") val avatar200: String
)