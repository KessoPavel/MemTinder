package com.radiance.domain.usecases

import android.app.Activity
import com.radiance.domain.repositories.MemRepository

class RegisterRepositoryUseCase(
    private val repository: MemRepository,
    private val activity: Activity
) {
    suspend fun toRegister() {
        if (!repository.isRegistered()){
            repository.toRegister(activity)
        }
    }
}