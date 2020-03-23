package com.radiance.domain.usecases

import com.radiance.domain.entity.Mem
import com.radiance.domain.repositories.MemRepository
import kotlinx.coroutines.flow.Flow

class GetRecommendedFlowUseCase(
    private val repository: MemRepository,
    private val requestStep: Int,
    private val fromStart: Boolean
) {
    fun get(): Flow<Mem> {
        return repository.startRecommendedMemFlow(requestStep, fromStart)
    }
}