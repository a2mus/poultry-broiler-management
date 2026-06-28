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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class HouseDimensionsMapperTest {
    @Test
    fun toEntity_thenToDomain_roundTripsAllFields() {
        val domain =
            HouseDimensions(
                id = "id-1",
                projectId = "project-1",
                length = Meters(50.0),
                width = Meters(10.0),
                wallHeight = Meters(3.0),
                roofType = RoofType.PITCHED,
                ridgeHeight = Meters(5.0),
                wallMaterial = WallMaterial.BLOCK,
                floorType = FloorType.CONCRETE,
                insulationType = InsulationType.POLYSTYRENE,
                insulationThickness = Millimeters(50.0),
                orientation = HouseOrientation.NE,
                createdAt = 1L,
                updatedAt = 2L,
            )

        val mapped = domain.toEntity().toDomain()

        assertEquals(domain.id, mapped.id)
        assertEquals(domain.projectId, mapped.projectId)
        assertEquals(domain.length.value, mapped.length.value)
        assertEquals(domain.width.value, mapped.width.value)
        assertEquals(domain.wallHeight.value, mapped.wallHeight.value)
        assertEquals(domain.roofType, mapped.roofType)
        assertEquals(domain.ridgeHeight?.value, mapped.ridgeHeight?.value)
        assertEquals(domain.wallMaterial, mapped.wallMaterial)
        assertEquals(domain.floorType, mapped.floorType)
        assertEquals(domain.insulationType, mapped.insulationType)
        assertEquals(domain.insulationThickness?.value, mapped.insulationThickness?.value)
        assertEquals(domain.orientation, mapped.orientation)
        assertEquals(domain.createdAt, mapped.createdAt)
        assertEquals(domain.updatedAt, mapped.updatedAt)
        assertEquals(domain.floorArea.value, mapped.floorArea.value)
    }

    @Test
    fun toDomain_givenFlatRoofNullRidgeHeight_preservesNull() {
        val entity =
            HouseDimensionsEntity(
                id = "id-2",
                projectId = "project-2",
                length = 50.0,
                width = 10.0,
                wallHeight = 3.0,
                roofType = RoofType.FLAT.name,
                ridgeHeight = null,
                wallMaterial = WallMaterial.STEEL.name,
                floorType = FloorType.DIRT.name,
                insulationType = InsulationType.NONE.name,
                insulationThickness = null,
                orientation = HouseOrientation.N.name,
                createdAt = 1L,
                updatedAt = 2L,
            )
        val domain = entity.toDomain()
        assertNull(domain.ridgeHeight)
        assertNull(domain.insulationThickness)
        assertEquals(RoofType.FLAT, domain.roofType)
        assertEquals(InsulationType.NONE, domain.insulationType)
    }

    @Test
    fun toEntity_givenNullOptionalFields_writesNullToEntity() {
        val domain =
            HouseDimensions(
                id = "id-3",
                projectId = "project-3",
                length = Meters(10.0),
                width = Meters(5.0),
                wallHeight = Meters(2.0),
                roofType = RoofType.ARCHED,
                ridgeHeight = null,
                wallMaterial = WallMaterial.PREFAB,
                floorType = FloorType.SLAT,
                insulationType = InsulationType.NONE,
                insulationThickness = null,
                orientation = HouseOrientation.S,
                createdAt = 0L,
                updatedAt = 0L,
            )
        val entity = domain.toEntity()
        assertNull(entity.ridgeHeight)
        assertNull(entity.insulationThickness)
        assertEquals(RoofType.ARCHED.name, entity.roofType)
        assertEquals(InsulationType.NONE.name, entity.insulationType)
    }

    @Test
    fun toDomain_storesEnumNamesPersistently_roundTrippedBack() {
        val domain =
            HouseDimensions(
                id = "id-4",
                projectId = "project-4",
                length = Meters(20.0),
                width = Meters(8.0),
                wallHeight = Meters(3.0),
                roofType = RoofType.PITCHED,
                ridgeHeight = Meters(4.0),
                wallMaterial = WallMaterial.STEEL,
                floorType = FloorType.SLAT,
                insulationType = InsulationType.MINERAL_WOOL,
                insulationThickness = Millimeters(100.0),
                orientation = HouseOrientation.SW,
                createdAt = 0L,
                updatedAt = 0L,
            )
        val entity = domain.toEntity()
        assertEquals("SW", entity.orientation)
        assertEquals("MINERAL_WOOL", entity.insulationType)
    }
}
