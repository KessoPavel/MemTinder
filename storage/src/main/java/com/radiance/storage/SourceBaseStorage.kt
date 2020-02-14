package com.radiance.storage

import com.radiance.core.Id
import com.radiance.core.Source

class SourceBaseStorage : SourceStorage {

    override fun saveSubscription(source: Source) {
        if (!subscription.contains(source)) {
            subscription.add(source)
        }
    }

    override fun saveAllSubscription(sourceList: List<Source>) {
        for (source in sourceList) {
            saveSubscription(source)
        }
    }

    override fun getSubscription(): List<Source> {
        return subscription
    }

    override fun saveRecommendation(source: Source) {
        if (!sources.contains(source)) {
            sources.add(source)
        }
    }

    override fun clearSubscription() {
        sources.clear()
    }

    override fun saveAllRecommendation(sourceList: List<Source>) {
        for (source in sourceList) {
            saveRecommendation(source)
        }
    }

    override fun getRecommendationById(id: Id): Source? {
        for (source in sources) {
            if (source.id == id) {
                return source
            }
        }
        return null
    }

    override fun getAllRecommendation(): List<Source> {
        return sources
    }

    override fun delete(id: Id) {
        for (source in sources) {
            if (source.id == id) {
                enabledSource.remove(source)
                sources.remove(source)
                break
            }
        }
    }

    override fun deleteAll() {
        sources.clear()
        enabledSource.clear()
    }

    override fun enableSource(source: Source, enable: Boolean) {
        if (enable) {
            if (!enabledSource.contains(source)) {
                enabledSource.add(source)
            }
        } else {
            enabledSource.remove(source)
        }
    }

    override fun getEnabledSource(): List<Source> {
        return enabledSource
    }

    companion object {
        private val subscription: ArrayList<Source> = ArrayList()
        private val sources: ArrayList<Source> = ArrayList()
        private val enabledSource: ArrayList<Source> = ArrayList()
    }
}