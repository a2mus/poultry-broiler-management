package com.poultry.broiler.data.repository

import app.cash.turbine.test
import com.poultry.broiler.data.local.dao.HouseDimensionsDao
import com.poultry.broiler.data.local.entity.HouseDimensionsEntity
import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.WallMaterial
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class HouseDimensionsRepositoryImplTest {
    private val dao: HouseDimensionsDao = mockk(relaxed = true)
    private val repository = HouseDimensionsRepositoryImpl(dao)

    private val entity =
        HouseDimensionsEntity(
            id = "id-1",
            projectId = "project-1",
            length = 100.0,
            width = 12.0,
            wallHeight = 3.0,
            roofType = RoofType.FLAT.name,
            ridgeHeight = null,
            wallMaterial = WallMaterial.BLOCK.name,
            floorType = FloorType.CONCRETE.name,
            insulationType = InsulationType.NONE.name,
            insulationThickness = null,
            orientation = HouseOrientation.N.name,
            createdAt = 1L,
            updatedAt = 2L,
        )

    @Test
    fun getDimensionsByProjectId_emitsEntityFromDao() =
        runTest {
            every { dao.getByProjectId("project-1") } returns flowOf(entity)
            repository.getDimensionsByProjectId("project-1").test {
                val result = awaitItem()
                assert(result != null)
                assertEqualsVal(result!!, entity)
                awaitComplete()
            }
        }

    @Test
    fun getDimensionsByProjectId_emitsNullWhenAbsent() =
        runTest {
            every { dao.getByProjectId("project-x") } returns flowOf(null)
            repository.getDimensionsByProjectId("project-x").test {
                org.junit.jupiter.api.Assertions.assertNull(awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun saveDimensions_delegatesToDaoUpsert() =
        runTest {
            val dimensions =
                HouseDimensions(
                    id = "id-1",
                    projectId = "project-1",
                    length = Meters(100.0),
                    width = Meters(12.0),
                    wallHeight = Meters(3.0),
                    roofType = RoofType.FLAT,
                    ridgeHeight = null,
                    wallMaterial = WallMaterial.BLOCK,
                    floorType = FloorType.CONCRETE,
                    insulationType = InsulationType.NONE,
                    insulationThickness = null,
                    orientation = HouseOrientation.N,
                    createdAt = 1L,
                    updatedAt = 2L,
                )
            repository.saveDimensions(dimensions)
            coVerify(exactly = 1) { dao.upsert(match { it.id == "id-1" }) }
        }

    @Test
    fun deleteDimensionsByProjectId_delegatesToDao() =
        runTest {
            coEvery { dao.deleteByProjectId("project-1") } returns Unit
            repository.deleteDimensionsByProjectId("project-1")
            coVerify(exactly = 1) { dao.deleteByProjectId("project-1") }
        }

    private fun assertEqualsVal(
        actual: HouseDimensions,
        expected: HouseDimensionsEntity,
    ) {
        org.junit.jupiter.api.Assertions.assertEquals(expected.id, actual.id)
        org.junit.jupiter.api.Assertions.assertEquals(expected.projectId, actual.projectId)
        org.junit.jupiter.api.Assertions.assertEquals(expected.orientation, actual.orientation.name)
        org.junit.jupiter.api.Assertions.assertEquals(expected.roofType, actual.roofType.name)
    }
}
