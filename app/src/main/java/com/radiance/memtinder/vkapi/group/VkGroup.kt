package com.radiance.memtinder.vkapi.group

import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.image.VkImage

data class VkGroup (
    val name: String,
    val avatar: VkImage,
    val id: VkId
)