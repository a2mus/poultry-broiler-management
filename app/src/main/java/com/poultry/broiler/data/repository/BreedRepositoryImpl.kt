package com.poultry.broiler.data.repository

import com.poultry.broiler.data.local.dao.BreedProfileDao
import com.poultry.broiler.data.mapper.toDomain
import com.poultry.broiler.domain.model.BreedProfile
import com.poultry.broiler.domain.repository.BreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedRepositoryImpl
    @Inject
    constructor(
        private val breedProfileDao: BreedProfileDao,
    ) : BreedRepository {
        override fun getAllBreeds(): Flow<List<BreedProfile>> {
            return breedProfileDao.getAll().map { entities ->
                entities.map { it.toDomain() }
            }
        }

        override suspend fun getBreedById(id: Long): BreedProfile? {
            return breedProfileDao.getById(id)?.toDomain()
        }

        override suspend fun getBreedByName(breedName: String): BreedProfile? {
            return breedProfileDao.getByName(breedName)?.toDomain()
        }
    }
