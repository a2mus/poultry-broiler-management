package com.poultry.broiler.data.mapper

import com.poultry.broiler.data.local.entity.HouseDimensionsEntity
import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.Millimeters
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.WallMaterial

/**
 * Maps a [HouseDimensionsEntity] to its domain [HouseDimensions] representation.
 *
 * Enum values are decoded with `valueOf` against their persisted `.name`; raw
 * numeric columns are wrapped in the domain primitives [Meters] / [Millimeters].
 */
fun HouseDimensionsEntity.toDomain(): HouseDimensions =
    HouseDimensions(
        id = id,
        projectId = projectId,
        length = Meters(length),
        width = Meters(width),
        wallHeight = Meters(wallHeight),
        roofType = RoofType.valueOf(roofType),
        ridgeHeight = ridgeHeight?.let(::Meters),
        wallMaterial = WallMaterial.valueOf(wallMaterial),
        floorType = FloorType.valueOf(floorType),
        insulationType = InsulationType.valueOf(insulationType),
        insulationThickness = insulationThickness?.let(::Millimeters),
        orientation = HouseOrientation.valueOf(orientation),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

/**
 * Maps a domain [HouseDimensions] to its [HouseDimensionsEntity] representation
 * for Room persistence.
 *
 * Enum values are stored via `.name` (never ordinal) and the primitives are
 * unwrapped to their raw `Double` columns.
 */
fun HouseDimensions.toEntity(): HouseDimensionsEntity =
    HouseDimensionsEntity(
        id = id,
        projectId = projectId,
        length = length.value,
        width = width.value,
        wallHeight = wallHeight.value,
        roofType = roofType.name,
        ridgeHeight = ridgeHeight?.value,
        wallMaterial = wallMaterial.name,
        floorType = floorType.name,
        insulationType = insulationType.name,
        insulationThickness = insulationThickness?.value,
        orientation = orientation.name,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
