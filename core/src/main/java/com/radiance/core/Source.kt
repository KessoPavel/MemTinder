package com.radiance.core

class Source private constructor(
    private val name: String,
    private val avatar: Image,
    private val id: Id
) {

    class Builder() {
        private var name: String = ""
        private var avatar: Image = Image.empty
        private var id: Id = Id.empty

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun avatar(avatar: Image): Builder {
            this.avatar = avatar
            return this
        }

        fun id(id: Id): Builder {
            this.id = id
            return Builder()
        }

        fun build(): Source {
            return Source(name, avatar, id)
        }
    }

    companion object {
        val empty = Source(
            "",
            Image.empty,
            Id.empty
        )
    }
}