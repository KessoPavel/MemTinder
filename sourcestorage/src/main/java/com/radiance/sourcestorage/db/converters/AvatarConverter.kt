package com.radiance.sourcestorage.db.converters

import androidx.room.TypeConverter
import com.radiance.sourcestorage.db.entity.Image
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class AvatarConverter {
    private val moshi = Moshi.Builder().build()
    private val type: ParameterizedType = Types.newParameterizedType(
        List::class.java,
        Image::class.java
    )
    private val avatarAdapter = moshi.adapter<List<Image>>(type)

    @TypeConverter
    fun avatarToJson(avatar: List<Image>) : String {
        return avatarAdapter.toJson(avatar)
    }

    @TypeConverter
    fun jsonToAvatar(string: String): List<Image> {
        return avatarAdapter.fromJson(string)?: ArrayList()
    }
}