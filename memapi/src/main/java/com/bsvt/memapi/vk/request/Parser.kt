package com.bsvt.memapi.vk.request

import com.radiance.core.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun JSONArray.getSourceList(): ArrayList<Source> {
    val answer = ArrayList<Source>()

    for (i in 0 until this.length()) {
        val group = this.getJSONObject(i)

        val name = group.name()
        val avatar = group.getAvatar()
        val id = Id.Builder().fromLong(group.id())

        val source = Source.Builder()
            .name(name)
            .avatar(avatar)
            .id(id)
            .build()

        answer.add(source)
    }

    return answer
}

fun JSONObject.name() = kotlin.run { this.getString("name") }

fun JSONObject.id() = kotlin.run { -this.getLong("id") }

fun JSONObject.getAvatar(): Image {
    return Image.Builder()
        .addImage(Resolution(50, 50), this.photo50())
        .addImage(Resolution(100, 100), this.photo100())
        .addImage(Resolution(200, 200), this.photo200())
        .build()
}

fun JSONObject.photo50() = kotlin.run { this.getString("photo_50") }

fun JSONObject.photo100() = kotlin.run { this.getString("photo_100") }

fun JSONObject.photo200() = kotlin.run { this.getString("photo_200") }

fun JSONArray.getMemes(): ArrayList<Mem> {
    val answer = ArrayList<Mem>()

    for (i in 0 until this.length()) {
        val memObject = this.getJSONObject(i)
        val mem = memObject.getMem()

        mem?.let {
            answer.add(it)
        }
    }

    return answer
}

fun JSONObject.nextFrom() = kotlin.run { this.getString("next_from") }

fun JSONObject.items() = kotlin.run { this.getJSONArray("items") }

fun JSONObject.responce() = kotlin.run { this.getJSONObject("response") }

fun JSONObject.responseArray() = kotlin.run { this.getJSONArray("response") }

fun JSONObject.getMem(): Mem? {
    try {
        val text = this.text()

        val attachment = this.attachments()
        val attachmentPhotos = attachment.getAttachments()

        val id = this.sourceId()
        val sourceId = Id.Builder().fromString(id)

        if (attachmentPhotos.isNotEmpty()) {
            return Mem.Builder()
                .title(text)
                .image(attachmentPhotos)
                .sourceId(sourceId)
                .build()
        }

        return null
    } catch (e: JSONException) {
        return null
    }
}

fun JSONArray.getAttachments(): ArrayList<Image> {
    val attachmentPhotos = ArrayList<Image>()

    for (i in 0 until this.length()) {
        val attachment = this.getJSONObject(i)
        attachment.getAttachment()?.let {
            attachmentPhotos.add(it)
        }
    }

    return attachmentPhotos
}

fun JSONObject.getAttachment(): Image? {
    this.takeIf { this.isPhoto() }?.let {
        val attachmentPhoto = it.photo()
        val sizes = attachmentPhoto.sizes()
        val imageBuilder = Image.Builder()

        for (i in 0 until sizes.length()) {
            val photo = sizes.getJSONObject(i)

            val width = photo.width()
            val height = photo.height()

            val resolution = Resolution(width, height)
            val url = photo.url()

            imageBuilder.addImage(resolution, url)
        }

        return imageBuilder.build()
    }

    return null
}

fun JSONObject.sourceId() = kotlin.run { this.getString("source_id") }

fun JSONObject.text() = kotlin.run { this.getString("text") }

fun JSONObject.attachments() = kotlin.run { this.getJSONArray("attachments") }

fun JSONObject.isPhoto() = kotlin.run { this.type() == "photo" }

fun JSONObject.type() = kotlin.run { this.getString("type") }

fun JSONObject.photo() = kotlin.run { this.getJSONObject("photo") }

fun JSONObject.sizes() = kotlin.run { this.getJSONArray("sizes") }

fun JSONObject.width() = kotlin.run { this.getInt("width") }

fun JSONObject.height() = kotlin.run { this.getInt("height") }

fun JSONObject.url() = kotlin.run { this.getString("url") }