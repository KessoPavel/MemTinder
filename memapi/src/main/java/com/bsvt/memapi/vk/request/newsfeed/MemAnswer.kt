package com.bsvt.memapi.vk.request.newsfeed

import com.radiance.core.Mem

data class MemAnswer(
    val memes: ArrayList<Mem>,
    val startFrom: String
) {
    companion object {
        val empty =
            MemAnswer(ArrayList(), "")
    }
}