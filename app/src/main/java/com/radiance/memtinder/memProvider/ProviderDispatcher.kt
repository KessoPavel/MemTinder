package com.radiance.memtinder.memProvider

import android.content.SharedPreferences
import com.radiance.memtinder.memProvider.news.IMemProvider
import com.radiance.memtinder.memProvider.news.MemProvider

object ProviderDispatcher {
    private var memProvider: MemProvider? = null

    fun getMemProvider(sharedPreference: SharedPreferences): IMemProvider {
        if (memProvider == null) {
            memProvider = MemProvider(
                sharedPreference
            )
        } else {
            memProvider?.updateSharedPreference(sharedPreference)
        }

        return memProvider!!
    }
}