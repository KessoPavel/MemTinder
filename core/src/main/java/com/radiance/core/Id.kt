package com.radiance.core

class Id private constructor(private val id: Long){

    fun toLong(): Long {
        return id
    }

    override fun toString(): String {
        return id.toString()
    }

    class Builder {
        fun fromString(string: String): Id {
            return Id(string.toLong())
        }

        fun fromLong(long: Long): Id {
            return Id(long)
        }

        fun fromInt(int: Int): Id {
            return Id(int.toLong())
        }
    }

    companion object {
        val empty = Id(0)
    }
}