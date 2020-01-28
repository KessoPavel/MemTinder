package com.radiance.memtinder.memProvider.news

import android.content.SharedPreferences
import com.radiance.memtinder.vkapi.api.GroupAnswer
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.MemesAnswer
import com.radiance.memtinder.vkapi.api.VkApi
import com.radiance.memtinder.vkapi.group.VkGroup

class MemProvider(private var sharedPreference: SharedPreferences): IMemProvider, IVkApi.MemesListener, IVkApi.GroupListener, IVkApi.RecommendedMemesListener {
    private var startFrom: String = ""
    private var enabledGroupIds = ""
    private val groups = ArrayList<VkGroup>()
    private val enabledGroup = ArrayList<VkGroup>()

    private val updateListenerList = ArrayList<IMemProvider.UpdateGroupListener>()
    private val memListenerList = ArrayList<IMemProvider.MemListener>()

    private var isLoaded: Boolean = false

    override fun load() {
        enabledGroupIds = sharedPreference.getString(ENABLED_GROUP_IDS_KEY, "").toString()

        VkApi.addMemesListener(this)
        VkApi.addRecommendedMemesListener(this)
        VkApi.addGroupListener(this)

        VkApi.requestGroups()
    }

    override fun isLoaded(): Boolean {
        return isLoaded
    }

    fun updateSharedPreference(sharedPreference: SharedPreferences) {
        this.sharedPreference = sharedPreference
    }

    override fun addUpdateListener(listener: IMemProvider.UpdateGroupListener) {
        if (!updateListenerList.contains(listener)) {
            updateListenerList.add(listener)
        }
    }

    override fun removeUpdateLister(listener: IMemProvider.UpdateGroupListener) {
        if (updateListenerList.contains(listener)) {
            updateListenerList.remove(listener)
        }
    }

    override fun addMemListener(listener: IMemProvider.MemListener) {
        if (!memListenerList.contains(listener)) {
            memListenerList.add(listener)
        }
    }

    override fun removeMemListener(listener: IMemProvider.MemListener) {
        if (memListenerList.contains(listener)) {
            memListenerList.remove(listener)
        }
    }

    override fun getGroups(): List<VkGroup> {
        return groups
    }

    override fun getEnabledGroup(): List<VkGroup> {
        return enabledGroup
    }

    override fun enableMemFromGroup(group: VkGroup, enable: Boolean) {
        if (enable) {
            if (!enabledGroup.contains(group)) {
                enabledGroup.add(group)
            }
        } else {
            if (enabledGroup.contains(group)) {
                enabledGroup.remove(group)
            }
        }

        saveEnabledGroup()
    }

    override fun clearEnabled() {
        enabledGroup.clear()
        saveEnabledGroup()
    }

    override fun enabledAll() {
        enabledGroup.clear()
        enabledGroup.addAll(groups)
        saveEnabledGroup()
    }

    override fun requestMemes(count: Int) {
        if (source == 0) {
            VkApi.requestMemes(enabledGroup, count, startFrom)
        } else {
            VkApi.requestRecommendedMemes(count, startFrom)
        }
    }

    override fun cleatStartFrom() {
        startFrom = ""
    }

    private var source = 0

    override fun recommendedNews() {
        source = 1
        startFrom = ""
    }

    override fun receiveGroup(answer: GroupAnswer) {
        isLoaded = true

        groups.clear()
        groups.addAll(answer.groups)

        if (enabledGroupIds != "") {
            val ids = enabledGroupIds.split(",")
            for (id in ids) {
                val group = getGroupBuyId(id)
                group?.let {
                    if (!enabledGroup.contains(it))
                        enabledGroup.add(it)
                }
            }
        }

        for (listener in updateListenerList) {
            listener.groupLoaded()
        }
    }

    override fun receiveMemes(answer: MemesAnswer) {
        startFrom = answer.nextFrom

        for (listener in memListenerList) {
            listener.receiveMemes(answer.memes)
        }
    }

    private fun getGroupBuyId(id: String): VkGroup? {
        for(group in groups) {
            if (group.id.getGroupId() == id) {
                return group
            }
        }
        return null
    }

    private fun saveEnabledGroup() {
        var ids = ""

        for (group in enabledGroup) {
            ids += group.id.getGroupId() + ","
        }

        val edit = sharedPreference.edit()

        edit.putString(ENABLED_GROUP_IDS_KEY, ids)
        edit.apply()
        enabledGroupIds = ids
    }

    companion object {
        private const val START_FROM_KEY = "start_from"
        private const val ENABLED_GROUP_IDS_KEY = "enabled_group_ids"
        const val FILE_NAME = "kek1"
    }
}