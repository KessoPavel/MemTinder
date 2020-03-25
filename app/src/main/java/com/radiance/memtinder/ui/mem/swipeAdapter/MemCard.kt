package com.radiance.memtinder.ui.mem.swipeAdapter

import com.radiance.domain.entity.Mem

data class MemCard(
    val url: String,
    val title: String,
    var groupName: String,
    val imagesCount: String,
    val mem: Mem
)