package com.poultry.broiler.domain.repository

import com.poultry.broiler.domain.model.Project
import kotlinx.coroutines.flow.Flow

/**
 * Domain-level contract for all project data operations.
 *
 * Read methods return [Flow] for reactive observation; write methods are
 * suspending one-shot operations.
 */
interface ProjectRepository {

    /**
     * Returns all projects ordered by [Project.updatedAt] descending
     * (most recently modified first). Emits a new list on any table change.
     */
    fun getAllProjectsSortedByModified(): Flow<List<Project>>

    /**
     * Returns projects whose [Project.name] or [Project.location] contains
     * [query] (case-insensitive, partial match), ordered by [Project.updatedAt]
     * descending. An empty [query] returns all projects.
     */
    fun searchProjects(query: String): Flow<List<Project>>

    /**
     * Returns a single project by [id], or `null` if not found.
     * Reactive: emits updates when the project changes.
     */
    fun getProjectById(id: String): Flow<Project?>

    /**
     * Inserts a new project. Returns the project ID.
     *
     * Precondition: [project.name] is non-empty (enforced at UseCase level).
     */
    suspend fun insertProject(project: Project): String

    /**
     * Updates an existing project's mutable fields (name, location, status, updatedAt).
     * The [Project.type] field is NOT updated (immutable after creation).
     */
    suspend fun updateProject(project: Project)

    /**
     * Permanently deletes the project with the given [id].
     * No-op if the project does not exist (idempotent).
     */
    suspend fun deleteProject(id: String)
}
