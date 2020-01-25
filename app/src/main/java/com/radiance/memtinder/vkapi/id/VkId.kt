package com.radiance.memtinder.vkapi.id

class VkId(private val id: Long) {

    fun getGroupId(): String {
        return "-$id"
    }
}