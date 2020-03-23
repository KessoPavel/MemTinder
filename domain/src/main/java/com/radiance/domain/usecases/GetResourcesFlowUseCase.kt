package com.radiance.domain.usecases

import com.radiance.domain.entity.Source
import com.radiance.domain.repositories.MemRepository
import kotlinx.coroutines.flow.Flow

class GetResourcesFlowUseCase (private val repository: MemRepository) {
    fun get(): Flow<List<Source>> {
        return repository.requestSources()
    }
}