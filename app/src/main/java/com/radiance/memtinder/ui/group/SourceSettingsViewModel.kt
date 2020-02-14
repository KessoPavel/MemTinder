package com.radiance.memtinder.ui.group

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsvt.memapi.MemApi
import com.bsvt.memapi.SourceType
import com.bsvt.memapi.vk.VkMemApi
import com.radiance.core.Mem
import com.radiance.core.Source
import com.radiance.storage.SourceStorage
import com.radiance.storage.StorageDispatcher

class SourceSettingsViewModel: ViewModel(), MemApi.MemApiListener {
    private lateinit var storage: SourceStorage
    private lateinit var memProvider: MemApi

    val sources: MutableLiveData<ArrayList<Source>> = MutableLiveData()
    val enabledSources: MutableLiveData<ArrayList<Source>> = MutableLiveData()

    fun init(activity: Activity) {
        storage = StorageDispatcher().createStorage(activity.applicationContext, StorageDispatcher.Storage.ROOM)

        //val sourceStorage = StorageDispatcher().createStorage(StorageDispatcher.Storage.BASE)
        memProvider = VkMemApi(storage)

        memProvider.addStateListener(this)

        if (memProvider.isRegistered()) {
            sources.value = ArrayList(storage.getSubscription())
            enabledSources.value = ArrayList(storage.getEnabledSource())
        } else {
            memProvider.toRegister(activity, object : MemApi.AuthorizationListener {
                override fun isAuthorize(authorize: Boolean) {
                    memProvider.requestSources()
                }
            })
        }
    }

    fun enableSource(source: Source, enable: Boolean) {
        memProvider.enabledSource(source, enable)
    }

    fun enableAll(enable: Boolean) {
        if (enable) {
            memProvider.enabledAll()
        } else {
            memProvider.disabledAll()
        }
    }

    override fun subscriptionUpdate() {
        sources.value = ArrayList(storage.getSubscription())
    }

    override fun sourcesUpdate() {
    }

    override fun enabledSourcesUpdate() {
        enabledSources.value = ArrayList(storage.getEnabledSource())
    }

    override fun memes(sourceType: SourceType, memes: List<Mem>) {
    }
}