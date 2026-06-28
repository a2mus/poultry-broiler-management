package com.poultry.broiler.domain.repository

import com.poultry.broiler.domain.model.BreedProfile
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    fun getAllBreeds(): Flow<List<BreedProfile>>

    suspend fun getBreedById(id: Long): BreedProfile?

    suspend fun getBreedByName(breedName: String): BreedProfile?
}
