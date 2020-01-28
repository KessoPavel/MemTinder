package com.radiance.memtinder.rating

class Rating(rating: Int){
    private val ratingName: String

    init {
        ratingName = when(rating) {
            LIKE -> "like"
            DISLIKE -> "dislike"
            else -> "unknown"
        }
    }

    fun getRating(): String {
        return ratingName
    }

    companion object {
        const val LIKE = 0
        const val DISLIKE = 1
    }
}