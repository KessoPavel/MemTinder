package com.radiance.domain.usecases

import com.radiance.domain.entity.Id
import com.radiance.domain.repositories.MemRepository

class RequestSourceInformationUseCase (private val repository: MemRepository, private val id: Id) {
    fun request() {
        repository.requestSource(id)
    }
}