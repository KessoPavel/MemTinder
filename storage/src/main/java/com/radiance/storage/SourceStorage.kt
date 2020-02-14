package com.radiance.storage

import com.radiance.core.Id
import com.radiance.core.Source

interface SourceStorage {
    fun saveSubscription(source: Source)
    fun saveAllSubscription(sourceList: List<Source>)
    fun getSubscription(): List<Source>

    fun saveRecommendation(source: Source)
    fun saveAllRecommendation(sourceList: List<Source>)
    fun getRecommendationById(id: Id): Source?
    fun getAllRecommendation(): List<Source>

    fun delete(id: Id)
    fun deleteAll()

    fun enableSource(source: Source, enable: Boolean)
    fun getEnabledSource(): List<Source>
}