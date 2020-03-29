package com.radiance.memtinder.ui.mem

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsvt.login.Login
import com.bsvt.mark.Mark
import com.radiance.data.repositories.VkMemRepository
import com.radiance.domain.entity.Mem
import com.radiance.domain.entity.Source
import com.radiance.domain.repositories.MemRepository
import com.radiance.domain.usecases.GetNewsFeedFlowUseCase
import com.radiance.domain.usecases.GetRecommendedFlowUseCase
import com.radiance.domain.usecases.GetResourcesFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext


class MemSwipeFragmentViewModel : ViewModel() {
    val newsfeed: MutableLiveData<ArrayList<Mem>> = MutableLiveData()
    val recommended: MutableLiveData<ArrayList<Mem>> = MutableLiveData()
    val sourcesList: MutableLiveData<ArrayList<Source>> = MutableLiveData()
    val enabledSourceList: MutableLiveData<ArrayList<Source>> = MutableLiveData()

    private var memProvider: MemRepository? = null

    var request = false
    var count = 0

    private var memFlow: Flow<Mem>? = null
    private var recommendedFlow: Flow<Mem>? = null


    suspend fun login(activity: Activity) {
        memProvider = VkMemRepository(activity.applicationContext)
        withContext(Dispatchers.Main) {
            newsfeed.value = ArrayList()
            recommended.value = ArrayList()
            sourcesList.value = ArrayList()
            enabledSourceList.value = ArrayList()
        }

        if (!memProvider?.isRegistered()!!) {
            memProvider?.toRegister(activity)
        } else {
            requestMem(10, true, SourceType.NEWS)

            memProvider?.let {
                val useCase = GetResourcesFlowUseCase(it)
                useCase.get().collect {
                    withContext(Dispatchers.Main) {
                        sourcesList.value = ArrayList(it)
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun requestMem(count: Int, fromStart: Boolean, source: SourceType) {
        Log.d("MEM_REQUEST", "VM: requestMem")
        if (memProvider?.isRegistered()?:false) {
            when (source) {
                SourceType.NEWS -> {
                    if (memFlow == null) {
                        memProvider?.let {
                            val useCase = GetNewsFeedFlowUseCase(it, count, fromStart)
                            memFlow = useCase.get()
                        }
                    }
                }
                SourceType.RECOMMENDED -> {
                    if (recommendedFlow == null) {
                        memProvider?.let {
                            val useCase = GetRecommendedFlowUseCase(it, count, fromStart)
                            recommendedFlow = useCase.get()
                        }
                    }
                }
            }
        }


        when (source) {
            SourceType.NEWS -> {
                memFlow?.take(count)?.collect {
                    newsfeed.value?.add(it)
                    withContext(Dispatchers.Main) {
                        newsfeed.value = newsfeed.value
                    }
                }
            }
            SourceType.RECOMMENDED -> {
                recommendedFlow?.take(count)?.collect {
                    recommended.value?.add(it)
                    withContext(Dispatchers.Main) {
                        recommended.value = recommended.value
                    }
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

enum class SourceType {
    NEWS,
    RECOMMENDED
}