package com.poultry.broiler.domain.model

/**
 * Represents the category of a broiler house project.
 *
 * The type is immutable after project creation and determines the
 * post-creation navigation target:
 * - [NEW_INSTALLATION] navigates to the design wizard.
 * - [EXISTING_ASSESSMENT] navigates to the dashboard.
 *
 * @property displayNameFr The French display label shown in the UI.
 * @property badgeText The short badge text shown on project cards.
 */
enum class ProjectType(
    val displayNameFr: String,
    val badgeText: String,
) {
    NEW_INSTALLATION(
        displayNameFr = "Nouvelle Installation",
        badgeText = "NEW",
    ),
    EXISTING_ASSESSMENT(
        displayNameFr = "Évaluation Existante",
        badgeText = "EXISTING",
    ),
}
