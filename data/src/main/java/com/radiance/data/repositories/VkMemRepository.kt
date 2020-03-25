package com.radiance.data.repositories

import android.app.Activity
import android.content.Context
import com.radiance.data.vkapi.VkApi
import com.radiance.data.vkapi.vk.request.newsfeed.NewsfeedRequest
import com.radiance.data.vkapi.vk.request.recommended.RecommendedRequest
import com.radiance.domain.entity.Id
import com.radiance.domain.entity.Mem
import com.radiance.domain.entity.Source
import com.radiance.domain.repositories.MemRepository
import com.radiance.sourcestorage.contract.SourceStorage
import com.radiance.sourcestorage.db.entity.SourceStatus
import com.radiance.sourcestorage.impl.RoomSourceStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class VkMemRepository(context: Context) : MemRepository {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var newsStartFrom = ""
    private var newsStep = 10
    private var recommendedStartFrom = ""
    private var recommendedStep = 10

    private val sourceStorage: SourceStorage = RoomSourceStorage(context)

    private val newsChannel = Channel<Mem>()
    private val recommendedChannel = Channel<Mem>()
    private val sourceInfoChannel = Channel<Source>()

    private val receivedMemes = ArrayList<String>()

    init {
        listenNewsChannel()
        listenRecommendedChannel()
        listenSourceChannel()
        listenSourceInfoChannel()
    }

    override suspend fun toRegister(activity: Activity): Boolean {
        if (!isRegistered()) {
            VkApi.authorization(activity)
        }
        return true
    }

    override fun isRegistered(): Boolean {
        return VkApi.isAuthorize()
    }

    override fun startMemFlow(step: Int, fromStart: Boolean): Flow<Mem> {
        newsStep = step
        val startFrom = if (fromStart) "" else newsStartFrom

        requestNews(newsStep, startFrom)

        return flow {
            for (mem in newsChannel) {
                emit(mem)
            }
        }
    }

    override fun startRecommendedMemFlow(step: Int, fromStart: Boolean): Flow<Mem> {
        val startFrom = if (fromStart) "" else recommendedStartFrom

        requestRecommended(newsStep, startFrom)

        return flow {
            for (mem in recommendedChannel) {
                emit(mem)
            }
        }
    }

    override fun requestSources(): Flow<List<Source>> {
        VkApi.requestSources()
        return sourceStorage.getAllSource().map { it.toSource() }
    }

    override fun requestSource(id: Id): Flow<Source> {
        VkApi.requestSource(id)

        return flow {
            for (source in sourceInfoChannel) {
                emit(source)
            }
        }
    }

    override suspend fun enabledSource(source: Source, enabled: Boolean) {
        val entity = source.toEntity()
        entity.status = if (enabled) SourceStatus.Enabled else SourceStatus.Unknown
        sourceStorage.updateAllSource(entity)
    }

    private fun requestNews(step: Int, startFrom: String) {
        val request = NewsfeedRequest.Builder()
            .count(step)
            .source(ArrayList(ArrayList()))
            .startFrom(startFrom)
            .build()

        VkApi.requestNewsfeed(request)
    }

    private fun requestRecommended(step: Int, startFrom: String) {
        val request = RecommendedRequest.Builder().count(step).startFrom(startFrom).build()
        VkApi.requestRecommended(request)
    }

    private fun listenNewsChannel() {
        scope.launch {
            for (news in VkApi.newsChannel) {
                if (!receivedMemes.contains(news.startFrom)) {
                    receivedMemes.add(news.startFrom)
                    newsStartFrom = news.startFrom

                    for (mem in news.memes) {
                        newsChannel.send(mem)
                    }

                    requestNews(newsStep, newsStartFrom)
                }
            }
        }
    }


    private fun listenRecommendedChannel() {
        scope.launch {
            for (news in VkApi.recommendedChannel) {
                if (recommendedStartFrom != news.startFrom) {
                    recommendedStartFrom = news.startFrom

                    for (mem in news.memes) recommendedChannel.send(mem)

                    requestRecommended(recommendedStep, recommendedStartFrom)
                }
            }
        }
    }

    private fun listenSourceChannel() {
        scope.launch {
            for (answer in VkApi.sourceChannel) {
                answer.sources.toEntity().forEach {
                    sourceStorage.updateAllSource(it)
                }
            }
        }
    }

    private fun listenSourceInfoChannel() {
        scope.launch {
            for (source in VkApi.sourceInfoChannel) {
                sourceInfoChannel.send(source)
            }
        }
    }
}