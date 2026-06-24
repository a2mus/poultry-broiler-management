package com.poultry.broiler.data.repository

import com.poultry.broiler.data.local.dao.HouseDimensionsDao
import com.poultry.broiler.data.mapper.toDomain
import com.poultry.broiler.data.mapper.toEntity
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.repository.HouseDimensionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Room-backed implementation of [HouseDimensionsRepository].
 *
 * Bridges the data-layer [com.poultry.broiler.data.local.entity.HouseDimensionsEntity]
 * and the domain [HouseDimensions] using the [com.poultry.broiler.data.mapper.HouseDimensionsMapper]
 * extensions. The DAO does the heavy lifting; this class only converts between
 * the two representations (Constitution Art 1.2.1).
 *
 * @property houseDimensionsDao Room DAO performing the actual reads and writes.
 */
@Singleton
class HouseDimensionsRepositoryImpl @Inject constructor(
    private val houseDimensionsDao: HouseDimensionsDao,
) : HouseDimensionsRepository {

    override fun getDimensionsByProjectId(projectId: String): Flow<HouseDimensions?> {
        return houseDimensionsDao.getByProjectId(projectId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveDimensions(dimensions: HouseDimensions) {
        houseDimensionsDao.upsert(dimensions.toEntity())
    }

    override suspend fun deleteDimensionsByProjectId(projectId: String) {
        houseDimensionsDao.deleteByProjectId(projectId)
    }
}