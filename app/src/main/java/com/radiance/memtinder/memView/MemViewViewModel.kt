package com.radiance.memtinder.memView

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radiance.memtinder.memProvider.ProviderDispatcher
import com.radiance.memtinder.memProvider.IMemProvider
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.VkApi
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.memes.VkMemes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemViewViewModel : ViewModel(),
    IMemProvider.MemListener,
    IMemProvider.UpdateGroupListener,
    IVkApi.AuthorizationListener{
    val memesStore: MutableLiveData<ArrayList<VkMemes>> = MutableLiveData()
    val groups: MutableLiveData<List<VkGroup>> = MutableLiveData()

    init {
        memesStore.value = ArrayList()
    }

    private lateinit var memProvider: IMemProvider
    private var isLoaded = false
    private var loadCount = 0

    private var requestSender: Boolean = false

    fun init(sharedPreferences: SharedPreferences?) {
        VkApi.addAuthorizationListener(this)

        memProvider = ProviderDispatcher.getMemProvider(sharedPreferences!!)
        memProvider.addMemListener(this)
        memProvider.addUpdateListener(this)

        if (VkApi.isAuthorized()) {
            memProvider.load()
            isLoaded = true
        }
    }

    fun loadMem(count: Int) {
        if (isLoaded) {
            if (!requestSender) {
                memProvider.requestMemes(count)
                requestSender = true
            }
        } else {
            loadCount += count
        }
    }

    override fun receiveMemes(memes: List<VkMemes>) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                memesStore.value = ArrayList(memes)
                requestSender = false
            }
        }
    }

    override fun groupLoaded() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                groups.value = memProvider.getGroups()
            }
        }

        memProvider.enabledAll()
        if (loadCount != 0) {
            memProvider.requestMemes(loadCount)
            loadCount = 0
        }

        isLoaded = true
    }

    override fun isAuthorized(boolean: Boolean) {
        memProvider.load()
    }
}
