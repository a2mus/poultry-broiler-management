package com.poultry.broiler.di

import com.poultry.broiler.data.repository.BreedRepositoryImpl
import com.poultry.broiler.data.repository.EquipmentRepositoryImpl
import com.poultry.broiler.domain.repository.BreedRepository
import com.poultry.broiler.domain.repository.EquipmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindBreedRepository(impl: BreedRepositoryImpl): BreedRepository

    @Binds
    abstract fun bindEquipmentRepository(impl: EquipmentRepositoryImpl): EquipmentRepository
}
