package com.bsvt.memapi.core

class Source private constructor(
    private val name: String,
    private val avatar: Image,
    private val id: Id
) {

    class Builder() {
        private var name: String = ""
        private var avatar: Image = Image.baseImage
        private var id: Id = Id.baseId

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
}