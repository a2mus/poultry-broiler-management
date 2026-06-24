package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.repository.HouseDimensionsRepository
import com.poultry.broiler.domain.usecase.DimensionValidationFailure
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SaveHouseDimensionsUseCaseTest {

    private val repository: HouseDimensionsRepository = mockk(relaxed = true)
    private val useCase = SaveHouseDimensionsUseCase(
        repository = repository,
        validateHouseDimensionsUseCase = ValidateHouseDimensionsUseCase(),
    )

    private val input = HouseDimensions(
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
        updatedAt = 0L,
    )

    @Test
    fun invoke_returnsSuccessAndDelegatesToRepository() = runTest {
        val result = useCase(input)
        assertTrue(result.isSuccess)
        assertEquals("id-1", result.getOrNull()?.id)
        coVerify(exactly = 1) { repository.saveDimensions(any()) }
    }

    @Test
    fun invoke_updatesUpdatedAtTimestamp() = runTest {
        val result = useCase(input)
        assertTrue(result.isSuccess)
        assertTrue((result.getOrNull()?.updatedAt ?: 0L) > 0L)
    }

    @Test
    fun invoke_returnsFailureWithValidationErrors_whenInvalid() = runTest {
        val invalid = input.copy(length = com.poultry.broiler.domain.model.Meters(0.0))
        val result = useCase(invalid)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DimensionValidationFailure)
        assertTrue(
            (result.exceptionOrNull() as DimensionValidationFailure)
                .fieldErrors.isNotEmpty(),
        )
        coVerify(exactly = 0) { repository.saveDimensions(any()) }
    }

    @Test
    fun invoke_returnsFailureWhenRepositoryThrows() = runTest {
        io.mockk.coEvery { repository.saveDimensions(any()) } throws RuntimeException("boom")
        val result = useCase(input)
        assertTrue(result.isFailure)
        assertEquals("boom", result.exceptionOrNull()?.message)
    }
}