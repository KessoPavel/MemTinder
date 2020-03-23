package com.radiance.domain.entity

class Source private constructor(
    val name: String,
    val avatar: Image,
    val id: Id
) {

    class Builder {
        private var name: String = ""
        private var avatar: Image = Image.empty
        private var id: Id = Id.empty

        fun name(name: String) = apply { this.name = name }

        fun avatar(avatar: Image) = apply { this.avatar = avatar }

        fun id(id: Id) = apply{ this.id = id }

        fun build() = Source(name, avatar, id)
    }

    override fun equals(other: Any?): Boolean {
        return this.id == (other as Source).id
    }

    companion object {
        val empty = Builder()
            .name("")
            .avatar(Image.empty)
            .id(Id.empty)
            .build()
    }
}