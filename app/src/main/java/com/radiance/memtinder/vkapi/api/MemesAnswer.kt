package com.radiance.memtinder.vkapi.api

import com.radiance.memtinder.vkapi.memes.VkMemes

data class MemesAnswer(
    val memes: List<VkMemes>, //todo memes
    val nextFrom: String
)