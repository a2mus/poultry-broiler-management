package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.repository.ProjectRepository
import javax.inject.Inject

/**
 * Permanently deletes a project by its ID.
 *
 * The deletion is idempotent — deleting a non-existent project is a no-op.
 *
 * @return [Result.success] on completion.
 */
class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
) {

    suspend operator fun invoke(projectId: String): Result<Unit> {
        repository.deleteProject(projectId)
        return Result.success(Unit)
    }
}
