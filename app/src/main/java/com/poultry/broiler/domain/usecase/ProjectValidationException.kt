package com.poultry.broiler.domain.usecase

/**
 * Validation errors that can occur during project operations.
 */
sealed class ProjectValidationException(message: String) : Exception(message) {

    /** The project name is empty after trimming. */
    data object EmptyName : ProjectValidationException("Project name is required")

    /** The project name exceeds the maximum allowed length. */
    data object NameTooLong :
        ProjectValidationException("Project name must not exceed ${CreateProjectUseCase.MAX_NAME_LENGTH} characters")

    /** The location exceeds the maximum allowed length. */
    data object LocationTooLong :
        ProjectValidationException("Location must not exceed ${CreateProjectUseCase.MAX_LOCATION_LENGTH} characters")

    /** The referenced project was not found. */
    data object NotFound : ProjectValidationException("Project not found")
}
