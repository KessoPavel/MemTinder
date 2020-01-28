package com.radiance.memtinder.ui.groupAdapter

import com.radiance.memtinder.vkapi.group.VkGroup

data class GroupItem(
    val group: VkGroup,
    val isSelected: Boolean
)