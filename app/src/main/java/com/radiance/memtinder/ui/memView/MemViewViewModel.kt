package com.radiance.memtinder.ui.memView

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radiance.memtinder.memProvider.ProviderDispatcher
import com.radiance.memtinder.memProvider.news.IMemProvider
import com.radiance.memtinder.rating.Rating
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

    var memList: MutableLiveData<ArrayList<VkMemes>> = MutableLiveData()
    var groupList: MutableLiveData<List<VkGroup>> = MutableLiveData()

    init {
        memList.value = ArrayList()
    }

    private lateinit var memProvider: IMemProvider
    private var loadCount = 0
    private var requestSender: Boolean = false

    fun init(sharedPreferences: SharedPreferences?) {
        VkApi.addAuthorizationListener(this)

        memList = MutableLiveData()
        groupList = MutableLiveData()

        memList.value = ArrayList()

        memProvider = ProviderDispatcher.getMemProvider(sharedPreferences!!)
        memProvider.addMemListener(this)
        memProvider.addUpdateListener(this)
        memProvider.cleatStartFrom()

        if (VkApi.isAuthorized()) {
            if (!memProvider.isLoaded()) {
                memProvider.load()
            } else {
                groupList.value = ArrayList(memProvider.getGroups())
            }
        }
    }

    fun clear() {
        memList.value = ArrayList()
        memProvider.cleatStartFrom()
    }

    fun stop() {
        memProvider.removeMemListener(this)
        memProvider.removeUpdateLister(this)
    }

    fun requestMem(count: Int, fromStart: Boolean = false) {
        if (fromStart) {
            memProvider.cleatStartFrom()
        }

        if (memProvider.isLoaded()) {
            if (!requestSender) {
                memProvider.requestMemes(count)
                requestSender = true
            }
        } else {
            loadCount += count
        }
    }

    fun setRating(mem: VkMemes, rating: Rating) {
    }

    override fun receiveMemes(memes: List<VkMemes>) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                memList.value = ArrayList(memes)
                requestSender = false
            }
        }
    }

    override fun groupLoaded() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                groupList.value = memProvider.getGroups()
            }
        }

        if (loadCount != 0) {
            memProvider.requestMemes(loadCount)
            loadCount = 0
        }
    }

    override fun isAuthorized(boolean: Boolean) {
        memProvider.load()
    }

    fun recommendedNews() {
        clear()
        memProvider.recommendedNews()
    }
}
