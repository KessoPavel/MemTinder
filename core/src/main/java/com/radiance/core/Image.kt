package com.radiance.core

class Image private constructor(private val images: HashMap<Resolution, String>){

    fun getResolutionList(): List<Resolution> {
        return images.keys.toList()
    }

    fun getImageLink(resolution: Resolution): String? {
        return images[resolution]
    }

    fun getHighResolution() : Resolution {
        var best = images.keys.toList().get(0)

        for (resolution in images.keys.toList()) {
            if (resolution.height > best.height) {
                best = resolution
            }
        }

        return best
    }

    fun getLowResolution(): Resolution {
        var best = images.keys.toList().get(0)

        for (resolution in images.keys.toList()) {
            if (resolution.height < best.height) {
                best = resolution
            }
        }

        return best
    }

    class Builder {
        private var images: HashMap<Resolution, String> = HashMap()

        fun addImage(resolution: Resolution, link: String) = apply {
            images[resolution] = link
        }

        fun build(): Image {
            return Image(images)
        }
    }

    companion object {
        private val emptyMap = hashMapOf(Pair(Resolution.empty, ""))
        val empty =
            Image(emptyMap)
    }
}