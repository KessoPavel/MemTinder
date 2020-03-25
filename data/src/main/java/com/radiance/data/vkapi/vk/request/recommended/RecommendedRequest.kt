package com.radiance.data.vkapi.vk.request.recommended

class RecommendedRequest(
    val count: Int,
    val startFrom: String) {

    class Builder {
        private var count: Int = 0
        private var startFrom: String = ""

        fun count(count: Int) = apply { this.count = count }

        fun startFrom(startFrom: String) = apply { this.startFrom = startFrom }

        fun build() = RecommendedRequest(count, startFrom)
    }
}