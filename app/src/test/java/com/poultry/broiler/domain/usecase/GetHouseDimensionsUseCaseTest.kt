package com.poultry.broiler.domain.usecase

import app.cash.turbine.test
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.repository.HouseDimensionsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GetHouseDimensionsUseCaseTest {

    private val repository: HouseDimensionsRepository = mockk()
    private val useCase = GetHouseDimensionsUseCase(repository)

    @Test
    fun invoke_emitsNullWhenRepositoryEmitsNull() = runTest {
        every { repository.getDimensionsByProjectId("p-1") } returns flowOf(null)
        useCase("p-1").test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun invoke_emitsHouseDimensionsWhenRepositoryEmitsEntity() = runTest {
        val existing = HouseDimensions(
            id = "id-1",
            projectId = "p-1",
            length = com.poultry.broiler.domain.model.Meters(100.0),
            width = com.poultry.broiler.domain.model.Meters(12.0),
            wallHeight = com.poultry.broiler.domain.model.Meters(3.0),
            roofType = com.poultry.broiler.domain.model.RoofType.FLAT,
            ridgeHeight = null,
            wallMaterial = com.poultry.broiler.domain.model.WallMaterial.BLOCK,
            floorType = com.poultry.broiler.domain.model.FloorType.CONCRETE,
            insulationType = com.poultry.broiler.domain.model.InsulationType.NONE,
            insulationThickness = null,
            orientation = com.poultry.broiler.domain.model.HouseOrientation.N,
            createdAt = 1L,
            updatedAt = 2L,
        )
        every { repository.getDimensionsByProjectId("p-1") } returns flowOf(existing)
        useCase("p-1").test {
            org.junit.jupiter.api.Assertions.assertEquals(existing.id, awaitItem().id)
            awaitComplete()
        }
    }
}