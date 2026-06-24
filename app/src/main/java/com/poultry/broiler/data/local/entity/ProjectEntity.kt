package com.poultry.broiler.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for the `projects` table.
 *
 * Stores all scalar fields of a [com.poultry.broiler.domain.model.Project].
 * Enum values are persisted as their string names.
 */
@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["updatedAt"]),
    ],
)
data class ProjectEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "status", defaultValue = "DRAFT")
    val status: String,

    @ColumnInfo(name = "location")
    val location: String?,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long,

    @ColumnInfo(name = "syncTimestamp")
    val syncTimestamp: Long?,
)
