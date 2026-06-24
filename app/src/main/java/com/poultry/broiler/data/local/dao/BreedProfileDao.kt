package com.poultry.broiler.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.poultry.broiler.data.local.entity.BreedProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedProfileDao {

    @Query("SELECT * FROM breed_profiles ORDER BY breed_name ASC")
    fun getAll(): Flow<List<BreedProfileEntity>>

    @Query("SELECT * FROM breed_profiles WHERE id = :id")
    suspend fun getById(id: Long): BreedProfileEntity?

    @Query("SELECT * FROM breed_profiles WHERE breed_name = :breedName")
    suspend fun getByName(breedName: String): BreedProfileEntity?

    @Query("SELECT COUNT(*) FROM breed_profiles")
    suspend fun count(): Int
}
