package com.poultry.broiler.domain.model

/**
 * The top-level organizational unit of the application.
 *
 * A Project groups all design configurations, calculations, and reports
 * for a single broiler house. It is the primary entity managed on the
 * Home Screen.
 *
 * @property id Unique identifier (UUID string).
 * @property name User-provided project name (non-empty, ≤ 200 characters).
 * @property type Project category; immutable after creation.
 * @property status Lifecycle status (defaults to [ProjectStatus.DRAFT]).
 * @property location Optional free-text location description.
 * @property createdAt Creation timestamp in epoch milliseconds.
 * @property updatedAt Last modification timestamp in epoch milliseconds.
 * @property syncTimestamp Reserved for future cloud sync; null when never synced.
 */
data class Project(
    val id: String,
    val name: String,
    val type: ProjectType,
    val status: ProjectStatus,
    val location: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncTimestamp: Long?,
)
