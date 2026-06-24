package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns all projects sorted by most-recently-modified first.
 *
 * Delegates to [ProjectRepository.getAllProjectsSortedByModified].
 */
class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository,
) {

    operator fun invoke(): Flow<List<Project>> {
        return repository.getAllProjectsSortedByModified()
    }
}
