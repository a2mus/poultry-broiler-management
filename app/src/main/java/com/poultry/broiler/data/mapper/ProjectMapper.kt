package com.poultry.broiler.data.mapper

import com.poultry.broiler.data.local.entity.ProjectEntity
import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.model.ProjectStatus
import com.poultry.broiler.domain.model.ProjectType

/**
 * Maps a [ProjectEntity] to its domain [Project] representation.
 */
fun ProjectEntity.toDomain(): Project =
    Project(
        id = id,
        name = name,
        type = ProjectType.valueOf(type),
        status = ProjectStatus.valueOf(status),
        location = location,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncTimestamp = syncTimestamp,
    )

/**
 * Maps a domain [Project] to its [ProjectEntity] representation for Room storage.
 */
fun Project.toEntity(): ProjectEntity =
    ProjectEntity(
        id = id,
        name = name,
        type = type.name,
        status = status.name,
        location = location,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncTimestamp = syncTimestamp,
    )
