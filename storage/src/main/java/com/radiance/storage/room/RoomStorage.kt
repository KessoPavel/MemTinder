package com.radiance.storage.room

import android.content.Context
import androidx.room.Room
import com.radiance.core.Id
import com.radiance.core.Source
import com.radiance.storage.*
import com.radiance.storage.room.dataBase.SourceDataBase
import com.radiance.storage.room.entity.EnabledId

class RoomStorage(context: Context) : SourceStorage {
    var db: SourceDataBase = Room.databaseBuilder(context, SourceDataBase::class.java, "sources_data_base").allowMainThreadQueries().build()

    init {
        db.subscriptionDao().deleteAll()
    }

    override fun saveSubscription(source: Source) {
        db.subscriptionDao().insertAll(source.toSubDao())
    }

    override fun saveAllSubscription(sourceList: List<Source>) {
        for (source in sourceList) {
            saveSubscription(source)
        }
    }

    override fun getSubscription(): List<Source> {
        val sources = db.subscriptionDao().getAll()
        val answer = ArrayList<Source>()

        for (source in sources) {
            answer.add(source.toSource())
        }

        return answer
    }

    override fun saveRecommendation(source: Source) {
        db.recommendedDao().insertAll(source.toDao())

    }

    override fun saveAllRecommendation(sourceList: List<Source>) {
        for (source in sourceList) {
            saveRecommendation(source)
        }
    }

    override fun getRecommendationById(id: Id): Source? {
        val sources = db.recommendedDao().getByIds(id.toLong())
        if (sources.size == 1)
            return sources[0].toSource()
        else
            return null
    }

    override fun getAllRecommendation(): List<Source> {
        val sources = db.recommendedDao().getAll()
        val answer = ArrayList<Source>()

        for (source in sources) {
            answer.add(source.toSource())
        }

        return answer
    }

    override fun delete(id: Id) {
    }

    override fun deleteAll() {
    }

    override fun enableSource(source: Source, enable: Boolean) {
        if (enable) {
            db.enabledDao().insertAll(EnabledId(0, source.id.toLong()))
        } else {
            val enabledIds = db.enabledDao().getByIds(source.id.toLong())
            if (enabledIds.size == 1)
                db.enabledDao().delete(enabledIds[0])
        }
    }

    override fun getEnabledSource(): List<Source> {
        val enabledIds = db.enabledDao().getAll()
        val enabledVkIds = ArrayList<Id>()
        for (id in enabledIds) {
            enabledVkIds.add(id.toId())
        }

        val answer = ArrayList<Source>()
        val sources = getSubscription()

        for (id in enabledVkIds) {
            val source = findSourceById(ArrayList(sources), id)
            if (source != null) {
                answer.add(source)
            }
        }

        return answer
    }

    fun findSourceById(sources: ArrayList<Source>, id: Id): Source? {
        for (source in sources) {
            if (source.id == id)
                return source
        }

        return null
    }
}