package com.radiance.storage

import com.radiance.core.Id
import com.radiance.core.Source

interface SourceStorage {
    fun saveSubscription(source: Source)
    fun saveAllSubscription(sourceList: List<Source>)
    fun getSubsctiption(): List<Source>

    fun save(source: Source)
    fun saveAll(sourceList: List<Source>)

    fun getById(id: Id): Source?
    fun getAll(): List<Source>

    fun delete(id: Id)
    fun deleteAll()

    fun enableSource(source: Source, enable: Boolean)
    fun getEnabledSource(): List<Source>
}