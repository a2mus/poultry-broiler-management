package com.poultry.broiler.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for the `house_dimensions` table.
 *
 * Stores the scalar fields of a
 * [com.poultry.broiler.domain.model.HouseDimensions] record. Enum values are
 * persisted as their string `.name` rather than ordinal, which keeps the
 * schema resilient to enum reordering (Constitution Art 2.3).
 *
 * Relationship: 1:1 with [ProjectEntity]. The unique index on [projectId]
 * enforces the single-dimensions-per-project rule, and `CASCADE` delete
 * guarantees that removing a project also removes its dimensions.
 *
 * @property id Primary key (UUID).
 * @property projectId Foreign key referencing `projects.id`; unique.
 * @property length Building length in meters.
 * @property width Building width in meters.
 * @property wallHeight Wall height in meters.
 * @property roofType Roof type enum name.
 * @property ridgeHeight Ridge height in meters; nullable for non-pitched roofs.
 * @property wallMaterial Wall material enum name.
 * @property floorType Floor type enum name.
 * @property insulationType Insulation type enum name.
 * @property insulationThickness Insulation thickness in millimeters; nullable when not applicable.
 * @property orientation House orientation enum name.
 * @property createdAt Creation timestamp (epoch millis).
 * @property updatedAt Last modification timestamp (epoch millis).
 */
@Entity(
    tableName = "house_dimensions",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["projectId"], unique = true)],
)
data class HouseDimensionsEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "projectId") val projectId: String,
    val length: Double,
    val width: Double,
    val wallHeight: Double,
    val roofType: String,
    val ridgeHeight: Double?,
    val wallMaterial: String,
    val floorType: String,
    val insulationType: String,
    val insulationThickness: Double?,
    val orientation: String,
    val createdAt: Long,
    val updatedAt: Long,
)
