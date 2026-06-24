package com.poultry.broiler.di

import com.poultry.broiler.data.repository.HouseDimensionsRepositoryImpl
import com.poultry.broiler.domain.repository.HouseDimensionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for the House Dimensions Wizard (Feature #003) bindings.
 *
 * The [HouseDimensionsDao] is already provided by [DatabaseModule]; this module
 * binds the [HouseDimensionsRepository] interface to its
 * [HouseDimensionsRepositoryImpl] implementation. Use cases are concrete
 * classes with `@Inject constructor` and are therefore auto-provided by Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WizardModule {

    @Binds
    abstract fun bindHouseDimensionsRepository(
        impl: HouseDimensionsRepositoryImpl,
    ): HouseDimensionsRepository
}