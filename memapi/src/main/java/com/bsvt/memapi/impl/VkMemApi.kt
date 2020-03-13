package com.bsvt.memapi.impl

import android.app.Activity
import com.bsvt.memapi.contract.MemApi
import com.bsvt.memapi.vk.request.newsfeed.NewsfeedRequest
import com.bsvt.memapi.vk.request.recommended.RecommendedRequest
import com.radiance.core.Id
import com.radiance.core.Mem
import com.radiance.core.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class VkMemApi : MemApi {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var newsStartFrom = ""
    private var newsStep = 10
    private var recommendedStartFrom = ""
    private var recommendedStep = 10

    private val newsChannel = Channel<Mem>()
    private val recommendedChannel = Channel<Mem>()
    private val sourceChannel = Channel<List<Source>>()
    private val sourceInfoChannel = Channel<Source>()

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

    @ExperimentalCoroutinesApi
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
        recommendedStep = step
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

        return flow {
            for (sources in sourceChannel) {
                emit(sources)
            }
        }
    }

    override suspend fun requestSource(id: Id): Flow<Source> {
        VkApi.requestSource(id)

        return flow {
            for (source in sourceInfoChannel) {
                emit(source)
            }
        }
    }

    override suspend fun enabledSource(source: Source, enabled: Boolean) {
        TODO("not implemented")
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
                if (newsStartFrom != news.startFrom) {
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
                sourceChannel.send(answer.sources)
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
