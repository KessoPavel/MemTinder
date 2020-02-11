package com.radiance.memtinder.ui.mem

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsvt.memapi.MemApi
import com.bsvt.memapi.SourceType
import com.bsvt.memapi.vk.AuthorizationActivity
import com.bsvt.memapi.vk.VkApi
import com.bsvt.memapi.vk.VkMemApi
import com.radiance.core.Mem
import com.radiance.core.Source
import com.radiance.storage.SourceStorage
import com.radiance.storage.StorageDispatcher

class MemSwipeFragmentViewModel: ViewModel(), MemApi.MemApiListener {
    val newsfeed: MutableLiveData<ArrayList<Mem>> = MutableLiveData()
    val recommended: MutableLiveData<ArrayList<Mem>> = MutableLiveData()
    val subscriptionList: MutableLiveData<ArrayList<Source>> = MutableLiveData()
    val sourcesList: MutableLiveData<ArrayList<Source>> = MutableLiveData()
    val enabledSourceList: MutableLiveData<ArrayList<Source>> = MutableLiveData()

    private lateinit var memProvider: MemApi
    private lateinit var storage: SourceStorage

    var request = false
    var count = 0
    var fromStart = false
    var sourceType = SourceType.NEWS


    fun login(activity: Activity) {
        storage = StorageDispatcher().createStorage(StorageDispatcher.Storage.BASE)

        val sourceStorage = StorageDispatcher().createStorage(StorageDispatcher.Storage.BASE)
        memProvider = VkMemApi(sourceStorage)

        memProvider.addStateListener(this)

        memProvider.toRegister(activity, object : MemApi.AuthorizationListener {
            override fun isAuthorize(authorize: Boolean) {
                memProvider.requestSources()

                if (request) {
                    memProvider.requestMem(count, fromStart, sourceType)
                    request = false
                }
            }
        })
    }

    fun requestMem(count: Int, fromStart: Boolean, source: SourceType) {
        if (memProvider.isRegistered()) {
            memProvider.requestMem(count, fromStart, source)
        } else {
            request = true
            this.count = count
            this.fromStart = fromStart
            this.sourceType = sourceType
        }
    }

    fun setRating(mem: Mem) {

    }

    override fun subscriptionUpdate() {
        subscriptionList.value = ArrayList(storage.getSubsctiption())
    }

    override fun sourcesUpdate() {
        sourcesList.value = ArrayList(storage.getAll())
        enabledSourceList.value = ArrayList(storage.getEnabledSource())
    }

    override fun enabledSourcesUpdate() {
        enabledSourceList.value = ArrayList(storage.getEnabledSource())
    }

    override fun memes(sourceType: SourceType, memes: List<Mem>) {
        when(sourceType) {
            SourceType.NEWS -> newsfeed.value = ArrayList(memes)
            SourceType.RECOMMENDED -> recommended.value = ArrayList(memes)
        }
    }
}