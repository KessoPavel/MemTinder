package com.radiance.storage

import android.content.Context
import com.radiance.storage.room.RoomStorage

class StorageDispatcher {
    fun createStorage(context: Context, type: Storage): SourceStorage {
        return when (type) {
            Storage.BASE -> SourceBaseStorage()
            Storage.ROOM -> RoomStorage(context)
        }
    }

    enum class Storage {
        BASE,
        ROOM
    }
}