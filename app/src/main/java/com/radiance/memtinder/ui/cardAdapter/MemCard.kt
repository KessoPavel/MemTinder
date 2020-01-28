package com.radiance.memtinder.ui.cardAdapter

import com.radiance.memtinder.vkapi.memes.VkMemes

data class MemCard(
    val url: String,
    val title: String,
    val groupName: String,
    val imagesCount: String,
    val mem: VkMemes
)