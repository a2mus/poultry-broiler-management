package com.poultry.broiler.presentation.home

import com.poultry.broiler.domain.model.Project

/**
 * Sealed UI state for the Home Screen.
 *
 * Follows Constitution Art 1.2.4 (UDF via StateFlow). Ensures exhaustive
 * `when` handling in the composable.
 */
sealed interface HomeUiState {
    /** Initial loading state. */
    data object Loading : HomeUiState

    /** No projects exist in the database. */
    data object Empty : HomeUiState

    /** Projects are available (or a search is active). */
    data class Content(
        val projects: List<Project>,
        val searchQuery: String,
        val isSearchActive: Boolean,
        val noSearchResults: Boolean,
    ) : HomeUiState

    /** A database error occurred. */
    data class Error(val message: String) : HomeUiState
}
