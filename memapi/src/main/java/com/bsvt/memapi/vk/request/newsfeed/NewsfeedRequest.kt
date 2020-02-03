package com.bsvt.memapi.vk.request.newsfeed

import com.radiance.core.Source

class NewsfeedRequest private constructor(
    val sources: ArrayList<Source>,
    val count: Int,
    val startFrom: String
) {

    class Builder {
        private var sources: ArrayList<Source> = ArrayList()
        private var count: Int = 0
        private var startFrom: String = ""

        fun source(sources: ArrayList<Source>) = apply { this.sources = sources }

        fun count(count: Int) = apply { this.count = count }

        fun startFrom(startFrom: String) = apply { this.startFrom = startFrom }

        fun build() = NewsfeedRequest(
            sources,
            count,
            startFrom
        )
    }
}