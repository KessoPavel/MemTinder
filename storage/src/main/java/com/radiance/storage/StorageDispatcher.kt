package com.radiance.storage

import android.content.Context
import com.radiance.storage.room.RoomStorage

class StorageDispatcher {


    fun createStorage(context: Context, type: Storage): SourceStorage {
        return when (type) {
            Storage.BASE -> SourceBaseStorage()
            Storage.ROOM ->{
                if (roomStorage == null) {
                    roomStorage = RoomStorage(context)
                }

                roomStorage!!
            }
        }
    }

    companion object {
        private var roomStorage: SourceStorage? = null
    }

    enum class Storage {
        BASE,
        ROOM
    }
}