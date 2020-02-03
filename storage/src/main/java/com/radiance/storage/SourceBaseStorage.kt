package com.radiance.storage

import com.radiance.core.Id
import com.radiance.core.Source

class SourceBaseStorage: SourceStorage {

    override fun save(source: Source) {
        if (!sources.contains(source)) {
            sources.add(source)
        }
    }

    override fun saveAll(sourceList: List<Source>) {
        for (source in sourceList) {
            save(source)
        }
    }

    override fun getById(id: Id): Source? {
        for (source in sources) {
            if (source.id == id) {
                return source
            }
        }
        return null
    }

    override fun getAll(): List<Source> {
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

    override fun enableSource(id: Id, enable: Boolean) {
        val source = getById(id)

        source?.let {
            if (enable) {
                if (!enabledSource.contains(it)) {
                    enabledSource.add(it)
                } else {
                    enabledSource.remove(it)
                }
            }
        }
    }

    override fun getEnabledSource(): List<Source> {
        return enabledSource
    }

    companion object {
        private val sources: ArrayList<Source> = ArrayList()
        private val enabledSource: ArrayList<Source> = ArrayList()
    }
}