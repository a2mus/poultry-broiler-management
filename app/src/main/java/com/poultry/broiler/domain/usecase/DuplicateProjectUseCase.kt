package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.model.ProjectStatus
import com.poultry.broiler.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

/**
 * Creates a deep copy of an existing project.
 *
 * The copy receives a new UUID, the name is suffixed with " (Copy)", the
 * status is reset to [ProjectStatus.DRAFT], and fresh timestamps are set.
 * All other fields (type, location) are preserved from the source.
 *
 * @return [Result.success] with the new [Project], or
 *         [Result.failure] if the source project is not found.
 */
class DuplicateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
) {

    suspend operator fun invoke(sourceProjectId: String): Result<Project> {
        val source = repository.getProjectById(sourceProjectId).first()
            ?: return Result.failure(ProjectValidationException.NotFound)

        val now = System.currentTimeMillis()
        val copy = Project(
            id = UUID.randomUUID().toString(),
            name = "${source.name} (Copy)",
            type = source.type,
            status = ProjectStatus.DRAFT,
            location = source.location,
            createdAt = now,
            updatedAt = now,
            syncTimestamp = null,
        )

        repository.insertProject(copy)
        return Result.success(copy)
    }
}
