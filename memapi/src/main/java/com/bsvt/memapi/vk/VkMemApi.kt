package com.bsvt.memapi.vk

import android.app.Activity
import android.util.Log
import com.bsvt.memapi.MemApi
import com.bsvt.memapi.SourceType
import com.bsvt.memapi.vk.request.newsfeed.MemAnswer
import com.bsvt.memapi.vk.request.newsfeed.NewsfeedRequest
import com.bsvt.memapi.vk.request.recommended.RecommendedRequest
import com.bsvt.memapi.vk.request.source.SourceAnswer
import com.radiance.core.Id
import com.radiance.core.Source
import com.radiance.storage.SourceStorage
import com.vk.api.sdk.VKApiCallback

class VkMemApi(private val storage: SourceStorage) : MemApi {
    private val listenerList = ArrayList<MemApi.MemApiListener>()
    private val newsListener = MemListener(SourceType.NEWS, ArrayList(), storage, this)
    private val recommendedListener = MemListener(SourceType.RECOMMENDED, ArrayList(), storage, this)

    override fun toRegister(activity: Activity, listener: MemApi.AuthorizationListener) {
        VkApi.addListener(listener)

        if (!VkApi.isAuthorize()) {
            VkApi.authorization(activity)
        } else {
            listener.isAuthorize(true)
        }
    }

    override fun isRegistered(): Boolean {
        return VkApi.isAuthorize()
    }

    override fun requestMem(count: Int, fromStart: Boolean, sourceType: SourceType) {
        when (sourceType) {
            SourceType.NEWS -> requestNews(count, fromStart)
            SourceType.RECOMMENDED -> requestRecommended(count, fromStart)
        }
    }

    private fun requestRecommended(count: Int, fromStart: Boolean) {
        val startFrom = if (fromStart) "" else recommendedListener.startFrom
        val request = RecommendedRequest.Builder().count(count).startFrom(startFrom).build()

        VkApi.requestRecommended(request, recommendedListener)
    }

    private fun requestNews(count: Int, fromStart: Boolean) {
        val startFrom = if (fromStart) "" else newsListener.startFrom
        val request =
            NewsfeedRequest.Builder()
                .count(count)
                .source(ArrayList(storage.getEnabledSource()))
                .startFrom(startFrom)
                .build()

        VkApi.requestNewsfeed(request, newsListener)
    }

    override fun requestSources() {
        VkApi.requestSources(object : VKApiCallback<SourceAnswer> {
            override fun fail(error: Exception) {
                Log.e("MemApi", error.toString())
            }

            override fun success(result: SourceAnswer) {
                storage.clearSubscription()
                storage.saveAllSubscription(result.sources)
                notifySubscriptionsChange()
            }
        })
    }

    override fun requestSource(id: Id) {
        VkApi.requestSource(id, object : VKApiCallback<Source> {
            override fun fail(error: Exception) {
                Log.e("MemApi", error.message)
            }

            override fun success(result: Source) {
                storage.saveRecommendation(result)
                notifySourceChange()
            }
        })
    }

    override fun enabledSource(source: Source, enabled: Boolean) {
        storage.enableSource(source, enabled)
        notifyEnabledSourceChange()
    }

    override fun addStateListener(listener: MemApi.MemApiListener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener)

            newsListener.updateListenerList(listenerList)
            recommendedListener.updateListenerList(listenerList)
        }
    }

    override fun removeStateListener(listener: MemApi.MemApiListener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener)

            newsListener.updateListenerList(listenerList)
            recommendedListener.updateListenerList(listenerList)
        }
    }

    override fun disabledAll() {
        for (source in storage.getSubscription()) {
            storage.enableSource(source, false)
        }

        notifyEnabledSourceChange()
    }

    override fun enabledAll() {
        for (source in storage.getSubscription()) {
            storage.enableSource(source, true)
        }

        notifyEnabledSourceChange()
    }


    private fun notifySubscriptionsChange() {
        listenerList.forEach {
            it.subscriptionUpdate()
        }
    }


    private fun notifySourceChange() {
        listenerList.forEach {
            it.sourcesUpdate()
        }
    }

    private fun notifyEnabledSourceChange() {
        listenerList.forEach {
            it.enabledSourcesUpdate()
        }
    }

    class MemListener(
        private val sourceType: SourceType,
        private var listenerList: ArrayList<MemApi.MemApiListener>,
        private var storage: SourceStorage,
        private val api: VkMemApi
    ) : VKApiCallback<MemAnswer> {
        var startFrom = ""

        override fun fail(error: Exception) {
            Log.e("MemApi", error.message)
        }

        override fun success(result: MemAnswer) {
            startFrom = result.startFrom

            if (sourceType == SourceType.RECOMMENDED) {
                val sources = ArrayList(storage.getAllRecommendation())
                for (mem in result.memes) {
                    val source = getSourceById(sources, mem.sourceId)

                    if (source == null) {
                        api.requestSource(mem.sourceId)
                    }
                }
            }

            listenerList.forEach {
                it.memes(sourceType, result.memes)
            }
        }

        fun updateListenerList(listenerList: ArrayList<MemApi.MemApiListener>) {
            this.listenerList = listenerList
        }

        private fun getSourceById(sources: ArrayList<Source>, id: Id): Source? {
            for (group in sources) {
                if (group.id.toLong() == id.toLong()) {
                    return group
                }
            }
            return null
        }

    }
}