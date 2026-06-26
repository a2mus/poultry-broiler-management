package com.poultry.broiler.di

import com.poultry.broiler.data.repository.ProjectRepositoryImpl
import com.poultry.broiler.domain.repository.ProjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for Project Management bindings.
 *
 * Binds the [ProjectRepository] interface to its [ProjectRepositoryImpl]
 * implementation. Use cases are concrete classes with `@Inject constructor`
 * and are therefore auto-provided by Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ProjectModule {
    @Binds
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository
}
