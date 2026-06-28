package com.poultry.broiler.domain.repository

import com.poultry.broiler.domain.model.HouseDimensions
import kotlinx.coroutines.flow.Flow

/**
 * Domain-layer contract for persisting and retrieving a project's
 * [HouseDimensions] configuration.
 *
 * Implementations live in the data layer (Room-backed) and expose only the
 * operations required by the wizard view-model and use cases. The Domain layer
 * depends on this interface — never on Room directly (Constitution Art 7.3).
 */
interface HouseDimensionsRepository {
    /**
     * Emits the dimensions for [projectId], or `null` while none are stored.
     */
    fun getDimensionsByProjectId(projectId: String): Flow<HouseDimensions?>

    /**
     * Inserts or replaces the given [dimensions] for its associated project.
     */
    suspend fun saveDimensions(dimensions: HouseDimensions)

    /**
     * Removes the dimensions associated with [projectId], if any.
     */
    suspend fun deleteDimensionsByProjectId(projectId: String)
}
