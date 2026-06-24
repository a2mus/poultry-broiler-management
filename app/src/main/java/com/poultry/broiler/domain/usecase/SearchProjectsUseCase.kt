package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns projects whose name or location contains [query]
 * (case-insensitive, partial match).
 *
 * Delegates to [ProjectRepository.searchProjects].
 * The ViewModel applies a 300 ms debounce before invoking this UseCase.
 */
class SearchProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository,
) {

    operator fun invoke(query: String): Flow<List<Project>> {
        return repository.searchProjects(query)
    }
}
