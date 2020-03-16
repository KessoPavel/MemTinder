package com.radiance.memtinder.ui.mem

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsvt.login.Login
import com.bsvt.mark.Mark
import com.bsvt.memapi.SourceType
import com.bsvt.memapi.contract.MemApi
import com.bsvt.memapi.impl.MemRepository
import com.bsvt.memapi.impl.toSource
import com.bsvt.memapi.vk.VkMemApi
import com.radiance.core.Mem
import com.radiance.core.Source
import com.radiance.storage.StorageDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take


class MemSwipeFragmentViewModel : ViewModel() {
    val newsfeed: MutableLiveData<ArrayList<Mem>> = MutableLiveData()
    val recommended: MutableLiveData<ArrayList<Mem>> = MutableLiveData()
    val sourcesList: MutableLiveData<ArrayList<Source>> = MutableLiveData()
    val enabledSourceList: MutableLiveData<ArrayList<Source>> = MutableLiveData()

    private lateinit var memProvider: MemApi

    var request = false
    var count = 0
    var fromStart = false
    var sourceType = SourceType.NEWS

    private var memFlow: Flow<Mem>? = null
    private var recommendedFlow: Flow<Mem>? = null


    suspend fun login(activity: Activity) {
        memProvider = MemRepository(activity.applicationContext)

        if (!memProvider.isRegistered()) {
            memProvider.toRegister(activity)
        } else {
            memProvider.requestSources().collect {
                sourcesList.value = it.toSource()
            }
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun requestMem(count: Int, fromStart: Boolean, source: SourceType) {
        if (memProvider.isRegistered()) {
            when (source) {
                SourceType.NEWS -> {
                    if (memFlow == null) {
                        memFlow = memProvider.startMemFlow(count, fromStart)
                    }
                }
                SourceType.RECOMMENDED -> {
                    if (recommendedFlow == null) {
                        recommendedFlow = memProvider.startRecommendedMemFlow(count, fromStart)
                    }
                }
            }
        }


        when (source) {
            SourceType.NEWS -> {
                memFlow?.take(count)?.collect {
                    newsfeed.value?.add(it)
                }
            }
            SourceType.RECOMMENDED -> {
                recommendedFlow?.take(count)?.collect {
                    recommended.value?.add(it)
                }
            }
        }
    }

    fun setRating(
        mem: Mem,
        rating: String
    ) {
        val ref = Login.account?.displayName
        val name = "${mem.sourceId.toLong()}_${mem.postId}"

        Mark()
            .referenceName(ref)
            .markName(name)
            .mark(rating)
            .send()
    }
}