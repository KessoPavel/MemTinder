package com.radiance.storage

class StorageDispatcher {
    fun createStorage(type: Storage): SourceStorage {
        return when (type) {
            Storage.BASE -> SourceBaseStorage()
        }
    }

    enum class Storage {
        BASE,
    }
}