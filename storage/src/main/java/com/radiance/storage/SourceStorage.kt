package com.radiance.storage

import com.radiance.core.Id
import com.radiance.core.Source

interface SourceStorage {
    fun save(source: Source)
    fun saveAll(sourceList: List<Source>)

    fun getById(id: Id): Source?
    fun getAll(): List<Source>

    fun delete(id: Id)
    fun deleteAll()

    fun enableSource(id: Id, enable: Boolean)
    fun getEnabledSource(): List<Source>
}