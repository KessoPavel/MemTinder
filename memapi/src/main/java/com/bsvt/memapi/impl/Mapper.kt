package com.bsvt.memapi.impl

import com.radiance.core.Id
import com.radiance.core.Image
import com.radiance.core.Resolution
import com.radiance.core.Source
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


fun Image.toEntity(): List<com.radiance.sourcestorage.db.entity.Image> {
    val answer = ArrayList<com.radiance.sourcestorage.db.entity.Image>()

    for (resolution in getResolutionList()) {
        answer.add(com.radiance.sourcestorage.db.entity.Image(resolution.height, resolution.width, getImageLink(resolution)?:""))
    }

    return answer
}


fun List<com.radiance.sourcestorage.db.entity.Image>.toImage(): Image{
    val answer = Image.Builder()

    for (resolution in this) {
        answer.addImage(Resolution(resolution.height, resolution.width),resolution.source)
    }

    return answer.build()
}