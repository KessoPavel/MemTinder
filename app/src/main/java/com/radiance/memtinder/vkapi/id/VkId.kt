package com.radiance.memtinder.vkapi.id

class VkId(private val id: Long) {
    constructor(id: String) : this(id.removePrefix("-").toLong())

    fun getGroupId(): String {
        return "-$id"
    }
}