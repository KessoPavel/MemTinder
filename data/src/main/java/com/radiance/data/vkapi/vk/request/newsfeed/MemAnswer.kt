package com.radiance.data.vkapi.vk.request.newsfeed

import com.radiance.domain.entity.Mem

data class MemAnswer(
    val memes: ArrayList<Mem>,
    val startFrom: String
) {
    companion object {
        val empty =
            MemAnswer(ArrayList(), "")
    }
}