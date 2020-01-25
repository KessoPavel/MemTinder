package com.radiance.memtinder.vkapi.memes

import com.radiance.memtinder.vkapi.image.VkImage

data class VkMemes(
    val title: String,
    val images: ArrayList<VkImage>
)