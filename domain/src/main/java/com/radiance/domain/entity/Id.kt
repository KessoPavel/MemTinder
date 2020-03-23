package com.radiance.domain.entity

class Id private constructor(private val id: Long){

    fun toLong(): Long {
        return id
    }

    override fun toString(): String {
        return id.toString()
    }

    override fun equals(other: Any?): Boolean {
        return this.id == (other as Id).id
    }

    class Builder {
        fun fromString(string: String) = Id(string.toLong())

        fun fromLong(long: Long) = Id(long)

        fun fromInt(int: Int) = Id(int.toLong())
    }

    companion object {
        val empty = Builder().fromInt(0)
    }
}