package com.radiance.memtinder.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radiance.memtinder.memProvider.ProviderDispatcher
import com.radiance.memtinder.memProvider.news.IMemProvider
import com.radiance.memtinder.vkapi.group.VkGroup

class GroupSettingViewModel : ViewModel(),  IMemProvider.UpdateGroupListener {
    private lateinit var memProvider: IMemProvider

    val groups: MutableLiveData<ArrayList<VkGroup>> = MutableLiveData()
    val enabledGroup: MutableLiveData<ArrayList<VkGroup>> = MutableLiveData()

    fun init(sharedPreferences: SharedPreferences?) {
        memProvider = ProviderDispatcher.getMemProvider(sharedPreferences!!)
        memProvider.addUpdateListener(this)

        if (memProvider.isLoaded()) {
            groups.value = ArrayList(memProvider.getGroups())
            enabledGroup.value = ArrayList(memProvider.getEnabledGroup())
        }
    }

    fun getGroup(): ArrayList<VkGroup> {
        return ArrayList(memProvider.getGroups())
    }

    fun getEnabledGroup(): ArrayList<VkGroup> {
        return ArrayList(memProvider.getEnabledGroup())
    }

    fun enabledGroup(group: VkGroup, enabled: Boolean) {
        memProvider.enableMemFromGroup(group, enabled)

        enabledGroup.value = ArrayList(memProvider.getEnabledGroup())
    }

    fun enabledAll(enabled: Boolean) {
        if (enabled) {
            memProvider.enabledAll()
        } else {
            memProvider.clearEnabled()
        }

        enabledGroup.value = ArrayList(memProvider.getEnabledGroup())
    }

    override fun groupLoaded() {
        groups.value = ArrayList(memProvider.getGroups())
        enabledGroup.value = ArrayList(memProvider.getEnabledGroup())
    }
}
