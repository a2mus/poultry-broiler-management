package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.model.ProjectStatus
import com.poultry.broiler.domain.model.ProjectType
import com.poultry.broiler.domain.repository.ProjectRepository
import java.util.UUID
import javax.inject.Inject

/**
 * Creates a new project with the given name, type and optional location.
 *
 * Validates that [name] is non-empty (after trimming) and does not exceed
 * 200 characters, and that [location] (if provided) does not exceed 300
 * characters.
 *
 * @return [Result.success] with the created [Project], or
 *         [Result.failure] with a [ProjectValidationException].
 */
class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
) {

    suspend operator fun invoke(
        name: String,
        type: ProjectType,
        location: String?,
    ): Result<Project> {
        val trimmedName = name.trim()
        val trimmedLocation = location?.trim()?.takeIf { it.isNotEmpty() }

        if (trimmedName.isEmpty()) {
            return Result.failure(ProjectValidationException.EmptyName)
        }
        if (trimmedName.length > MAX_NAME_LENGTH) {
            return Result.failure(ProjectValidationException.NameTooLong)
        }
        if (trimmedLocation != null && trimmedLocation.length > MAX_LOCATION_LENGTH) {
            return Result.failure(ProjectValidationException.LocationTooLong)
        }

        val now = System.currentTimeMillis()
        val project = Project(
            id = UUID.randomUUID().toString(),
            name = trimmedName,
            type = type,
            status = ProjectStatus.DRAFT,
            location = trimmedLocation,
            createdAt = now,
            updatedAt = now,
            syncTimestamp = null,
        )

        repository.insertProject(project)
        return Result.success(project)
    }

    companion object {
        const val MAX_NAME_LENGTH = 200
        const val MAX_LOCATION_LENGTH = 300
    }
}
