package com.radiance.core

import android.service.quicksettings.Tile

class Mem private constructor(
    val title: String,
    val images: List<Image>,
    val sourceId: Id
) {
    class Builder {
        private var title: String = ""
        private var image = ArrayList<Image>()
        private var sourceId: Id = Id.empty

        fun title(title: String) = apply { this.title = title }

        fun image(images: ArrayList<Image>) = apply { this.image = images }

        fun sourceId(id: Id) = apply { this.sourceId = id }

        fun build() = Mem(title, image, sourceId)
    }

    companion object {
        val empty = Builder()
            .title("")
            .image(ArrayList<Image>().apply{ add(Image.empty)})
            .sourceId(Id.empty)
            .build()
    }
}