package com.radiance.core

data class Resolution(val height: Int, val width: Int) {
    companion object {
        val empty = Resolution(0, 0)
    }
}