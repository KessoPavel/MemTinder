package com.radiance.memtinder.vkapi.image

class VkImage private constructor(private val images: HashMap<Resolution, String>){

    fun getResolutionList(): List<Resolution> {
        return images.keys.toList()
    }

    fun getImageLink(resolution: Resolution): String? {
        return images[resolution]
    }

    class Builder {
        private var images: HashMap<Resolution, String> = HashMap()

        fun addImage(resolution: Resolution, link: String): Builder {
            images[resolution] = link

            return this
        }

        fun build(): VkImage {
            return VkImage(images)
        }
    }
}