package com.radiance.storage

import com.radiance.core.Id
import com.radiance.core.Image
import com.radiance.core.Resolution
import com.radiance.core.Source
import com.radiance.storage.room.entity.EnabledId
import com.radiance.storage.room.entity.RecommendedSource
import com.radiance.storage.room.entity.SubscriptionSource

fun EnabledId.toId(): Id {
    return Id.Builder().fromLong(this.vkId)
}

fun RecommendedSource.toSource(): Source {
    val id = Id.Builder().fromLong(this.vkId)
    val avatar = Image.Builder()
        .addImage(Resolution(50,50), this.avatar50)
        .addImage(Resolution(100,100), this.avatar100)
        .addImage(Resolution(200,200), this.avatar200)
        .build()

    return Source.Builder()
        .name(this.name)
        .id(id)
        .avatar(avatar)
        .build()
}

fun SubscriptionSource.toSource(): Source {
    val id = Id.Builder().fromLong(this.vkId)
    val avatar = Image.Builder()
        .addImage(Resolution(50,50), this.avatar50)
        .addImage(Resolution(100,100), this.avatar100)
        .addImage(Resolution(200,200), this.avatar200)
        .build()

    return Source.Builder()
        .name(this.name)
        .id(id)
        .avatar(avatar)
        .build()
}

fun Source.toDao(): RecommendedSource {
    val name = this.name
    val id = this.id.toLong()
    val avatar50 = this.avatar.getImageLink(avatar.getResolutionList()[0])!!
    val avatar100 = this.avatar.getImageLink(avatar.getResolutionList()[1])!!
    val avatar200 = this.avatar.getImageLink(avatar.getResolutionList()[2])!!

    return RecommendedSource(0, name, id, avatar50, avatar100, avatar200)
}

fun Source.toSubDao(): SubscriptionSource {
    val name = this.name
    val id = this.id.toLong()
    val avatar50 = this.avatar.getImageLink(avatar.getResolutionList()[0])!!
    val avatar100 = this.avatar.getImageLink(avatar.getResolutionList()[1])!!
    val avatar200 = this.avatar.getImageLink(avatar.getResolutionList()[2])!!

    return SubscriptionSource(0, name, id, avatar50, avatar100, avatar200)
}