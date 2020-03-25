package com.radiance.data.repositories

import com.radiance.domain.entity.Id
import com.radiance.domain.entity.Resolution
import com.radiance.domain.entity.Source
import com.radiance.sourcestorage.db.entity.Image
import com.radiance.sourcestorage.db.entity.SourceEntity
import com.radiance.sourcestorage.db.entity.SourceStatus

fun List<Source>.toEntity(): List<SourceEntity> {
    val answer = ArrayList<SourceEntity>()

    forEach {
        answer.add(it.toEntity())
    }

    return answer
}

fun List<SourceEntity>.toSource(): ArrayList<Source> {
    val answer = ArrayList<Source>()

    forEach {
        answer.add(it.toSource())
    }

    return answer
}

fun Source.toEntity(): SourceEntity {
    return SourceEntity(id.toLong(), name, avatar.toEntity(), SourceStatus.Unknown)
}


fun SourceEntity.toSource(): Source {
    return Source.Builder().id(Id.Builder().fromLong(vkId)).name(name).avatar(avatar.toImage()).build()
}


fun com.radiance.domain.entity.Image.toEntity(): List<Image> {
    val answer = ArrayList<Image>()

    for (resolution in getResolutionList()) {
        answer.add(Image(resolution.height, resolution.width, getImageLink(resolution)?:""))
    }

    return answer
}


fun List<Image>.toImage(): com.radiance.domain.entity.Image{
    val answer = com.radiance.domain.entity.Image.Builder()

    for (resolution in this) {
        answer.addImage(Resolution(resolution.height, resolution.width),resolution.source)
    }

    return answer.build()
}