package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.repository.HouseDimensionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Retrieves the persisted [HouseDimensions] for a single project, or `null`
 * while none have been saved yet. A thin pass-through over the repository
 * that keeps the presentation layer decoupled from Room (Constitution Art 7.3).
 */
class GetHouseDimensionsUseCase @Inject constructor(
    private val repository: HouseDimensionsRepository,
) {
    /**
     * @param projectId Foreign key referencing the owning project.
     * @return A [Flow] emitting the dimensions, or `null` while absent.
     */
    operator fun invoke(projectId: String): Flow<HouseDimensions?> =
        repository.getDimensionsByProjectId(projectId)
}