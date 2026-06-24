package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Updates an existing project's name and location.
 *
 * The project [type] and [com.poultry.broiler.domain.model.ProjectStatus]
 * are preserved. Validates the same constraints as
 * [CreateProjectUseCase] for name and location.
 *
 * @return [Result.success] with the updated [Project], or
 *         [Result.failure] if validation fails or the project is not found.
 */
class UpdateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
) {

    suspend operator fun invoke(
        projectId: String,
        name: String,
        location: String?,
    ): Result<Project> {
        val trimmedName = name.trim()
        val trimmedLocation = location?.trim()?.takeIf { it.isNotEmpty() }

        if (trimmedName.isEmpty()) {
            return Result.failure(ProjectValidationException.EmptyName)
        }
        if (trimmedName.length > CreateProjectUseCase.MAX_NAME_LENGTH) {
            return Result.failure(ProjectValidationException.NameTooLong)
        }
        if (trimmedLocation != null && trimmedLocation.length > CreateProjectUseCase.MAX_LOCATION_LENGTH) {
            return Result.failure(ProjectValidationException.LocationTooLong)
        }

        val existing = repository.getProjectById(projectId).first()
            ?: return Result.failure(ProjectValidationException.NotFound)

        val updated = existing.copy(
            name = trimmedName,
            location = trimmedLocation,
            updatedAt = System.currentTimeMillis(),
        )
        repository.updateProject(updated)
        return Result.success(updated)
    }
}
