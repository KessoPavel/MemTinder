package com.radiance.memtinder.memProvider

import android.content.SharedPreferences
import com.radiance.memtinder.vkapi.api.GroupAnswer
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.MemesAnswer
import com.radiance.memtinder.vkapi.api.VkApi
import com.radiance.memtinder.vkapi.group.VkGroup

class MemProvider(private var sharedPreference: SharedPreferences): IMemProvider, IVkApi.MemesListener, IVkApi.GroupListener {
    private var startFrom: String = ""
    private var enabledGroupIds = ""
    private val groups = ArrayList<VkGroup>()
    private val enabledGroup = ArrayList<VkGroup>()

    private val updateListenerList = ArrayList<IMemProvider.UpdateGroupListener>()
    private val memListenerList = ArrayList<IMemProvider.MemListener>()

    override fun load() {
        startFrom = sharedPreference.getString(START_FROM_KEY, "").toString()
        enabledGroupIds = sharedPreference.getString(ENABLED_GROUP_IDS_KEY, "").toString()

        VkApi.addMemesListener(this)
        VkApi.addGroupListener(this)

        VkApi.requestGroups()
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
        enabledGroup.addAll(groups)
        saveEnabledGroup()
    }

    override fun requestMemes(count: Int) {
        VkApi.requestMemes(enabledGroup, count, startFrom)
    }

    override fun receiveGroup(answer: GroupAnswer) {
        groups.clear()
        groups.addAll(answer.groups)

        if (enabledGroupIds != "") {
            val ids = enabledGroupIds.split(",")
            for (id in ids) {
                val group = getGroupBuyId(id)
                group?.let {
                    enabledGroup.add(it)
                }
            }
        } else {
            //enabledGroup.addAll(groups)
        }

        for (listener in updateListenerList) {
            listener.groupLoaded()
        }
    }

    override fun receiveMemes(answer: MemesAnswer) {
        startFrom = answer.nextFrom
        saveStartFrom()

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
            ids += group.id.getGroupId() + ", "
        }

        val edit = sharedPreference.edit()

        edit.putString(ENABLED_GROUP_IDS_KEY, ids)
        edit.apply()
        enabledGroupIds = ids
    }

    private fun saveStartFrom() {
        val edit = sharedPreference.edit()

        edit.putString(START_FROM_KEY, startFrom)
        edit.apply()
    }

    companion object {
        private const val START_FROM_KEY = "start_from"
        private const val ENABLED_GROUP_IDS_KEY = "enabled_group_ids"
        const val FILE_NAME = "kek1"
    }
}