package com.radiance.memtinder.ui.memView

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radiance.memtinder.memProvider.ProviderDispatcher
import com.radiance.memtinder.memProvider.news.IMemProvider
import com.radiance.memtinder.memProvider.news.Source
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

    var news: MutableLiveData<ArrayList<VkMemes>> = MutableLiveData()
    var recommended: MutableLiveData<ArrayList<VkMemes>> = MutableLiveData()
    var groupList: MutableLiveData<List<VkGroup>> = MutableLiveData()
    var recommendedGroup: MutableLiveData<List<VkGroup>> = MutableLiveData()

    init {
        news.value = ArrayList()
        recommended.value = ArrayList()
    }

    private lateinit var memProvider: IMemProvider
    private var loadCount = 0
    private var requestSender: Boolean = false
    private var currentSource: Source = Source.NEWS

    fun init(sharedPreferences: SharedPreferences?, source: Source) {
        VkApi.addAuthorizationListener(this)

        currentSource = source

        news = MutableLiveData()
        recommended = MutableLiveData()
        groupList = MutableLiveData()

        clearNews()
        clearRecommended()

        memProvider = ProviderDispatcher.getMemProvider(sharedPreferences!!)
        memProvider.addMemListener(this)
        memProvider.addUpdateListener(this)

        if (VkApi.isAuthorized()) {
            if (!memProvider.isLoaded()) {
                memProvider.load(currentSource)
            } else {
                memProvider.setSource(currentSource)
                groupList.value = ArrayList(memProvider.getGroups())
            }
        }
    }

    fun clear() {
        when (currentSource) {
            Source.NEWS -> clearNews()
            Source.RECOMMENDED -> clearRecommended()
        }
    }

    fun clearNews(){
        news.value = ArrayList()
    }

    fun clearRecommended() {
        recommended.value = ArrayList()
    }

    fun stop() {
        memProvider.removeMemListener(this)
        memProvider.removeUpdateLister(this)
    }

    fun requestMem(count: Int, fromStart: Boolean = false) {
        if (memProvider.isLoaded()) {
            if (!requestSender) {
                memProvider.requestMemes(count, fromStart)
                requestSender = true
            }
        } else {
            loadCount += count
        }
    }

    fun setRating(mem: VkMemes, rating: Rating) {
    }

    override fun receiveNews(memes: List<VkMemes>) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                news.value = ArrayList(memes)
                requestSender = false
            }
        }
    }

    override fun receiveRecommended(memes: List<VkMemes>) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                recommended.value = ArrayList(memes)
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

    override fun recommendedGroupUpdated() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                recommendedGroup.value = memProvider.getRecommendedGroup()
            }
        }
    }

    fun setSource(source: Source) {
        currentSource = source
        memProvider.setSource(currentSource)
    }

    override fun isAuthorized(boolean: Boolean) {
        memProvider.load(currentSource)
    }
}
