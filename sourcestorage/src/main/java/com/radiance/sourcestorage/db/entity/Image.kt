package com.radiance.sourcestorage.db.entity

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class Image (
    @Json(name = "height")
    val height: Int,
    @Json(name = "width")
    val width: Int,
    @Json(name = "source")
    val source: String
)