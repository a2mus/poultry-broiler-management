package com.poultry.broiler.domain.model

/**
 * Represents the lifecycle status of a project.
 *
 * Transitions are automatic and unidirectional:
 * [DRAFT] → [IN_PROGRESS] → [COMPLETED].
 *
 * Manual status changes are not supported. At this feature stage all
 * projects remain in [DRAFT]; status transitions activate with later features.
 *
 * @property displayNameFr The French display label shown in the UI.
 */
enum class ProjectStatus(
    val displayNameFr: String,
) {
    DRAFT(displayNameFr = "Brouillon"),
    IN_PROGRESS(displayNameFr = "En cours"),
    COMPLETED(displayNameFr = "Terminé"),
}
