package com.poultry.broiler.presentation.home

import com.poultry.broiler.domain.model.ProjectType

/**
 * Sealed class representing all user actions on the Home Screen.
 *
 * Events flow from UI → ViewModel via [HomeViewModel.onIntent].
 */
sealed class HomeIntent {
    /** Create a new project (FR-002). */
    data class CreateProject(
        val name: String,
        val type: ProjectType,
        val location: String?,
    ) : HomeIntent()

    /** Update the search query (FR-004). */
    data class UpdateSearchQuery(val query: String) : HomeIntent()

    /** Open a project by tapping its card (FR-008). */
    data class OpenProject(val projectId: String) : HomeIntent()

    /** Edit a project's name and location (FR-010). */
    data class EditProject(
        val projectId: String,
        val name: String,
        val location: String?,
    ) : HomeIntent()

    /** Duplicate a project (FR-011). */
    data class DuplicateProject(val projectId: String) : HomeIntent()

    /** Delete a project (FR-013). */
    data class DeleteProject(val projectId: String) : HomeIntent()

    /** Clear the search bar. */
    data object ClearSearch : HomeIntent()
}
