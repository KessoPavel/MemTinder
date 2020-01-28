package com.radiance.memtinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.radiance.memtinder.rating.Rating
import com.radiance.memtinder.vkapi.image.VkImage
import com.yuyakaido.android.cardstackview.Direction

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

fun VkImage.getMinResolutionImage(): String {
    val resolutions = this.getResolutionList()

    var min = resolutions[0].height
    var minIndex = 0
    for (resolution in resolutions) {
        if (resolution.height < min) {
            minIndex = resolutions.indexOf(resolution)
            min = resolution.height
        }
    }

    this.getImageLink(resolutions[minIndex])?.let {
        return it
    }

    return ""
}

fun Direction.toRating(): Rating {
    if (this == Direction.Left) {
        return Rating(Rating.DISLIKE)
    } else if (this == Direction.Right) {
        return Rating(Rating.LIKE)
    } else {
        return Rating(-1)
    }
}