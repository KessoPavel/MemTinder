package com.radiance.memtinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.radiance.memtinder.vkapi.image.VkImage

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun VkImage.getBestResolutionImage(): String {
    val resolutions = this.getResolutionList()

    var max = 0
    var maxIndex = 0
    for (resolution in resolutions) {
        if (resolution.height > max) {
            maxIndex = resolutions.indexOf(resolution)
            max = resolution.height
        }
    }

    this.getImageLink(resolutions[maxIndex])?.let {
        return it
    }

    return ""
}