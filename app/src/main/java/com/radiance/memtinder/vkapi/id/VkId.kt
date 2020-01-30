package com.radiance.memtinder.vkapi.id

class VkId(private val id: Long) {
    constructor(id: String) : this(id.toLong())

    fun getGroupId(): String {
        return "$id"
    }

    fun getPositiveId(): Long {
        if (id < 0)
            return -id
        return id
    }
}