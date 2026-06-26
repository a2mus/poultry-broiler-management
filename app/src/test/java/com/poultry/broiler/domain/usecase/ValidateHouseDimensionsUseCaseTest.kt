package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.Millimeters
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.WallMaterial
import com.poultry.broiler.domain.validation.DimensionErrorCode
import com.poultry.broiler.domain.validation.DimensionField
import com.poultry.broiler.domain.validation.DimensionValidationResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidateHouseDimensionsUseCaseTest {
    private val useCase = ValidateHouseDimensionsUseCase()

    private fun validDimensions(
        roofType: RoofType = RoofType.FLAT,
        ridgeHeight: Meters? = null,
        insulationType: InsulationType = InsulationType.NONE,
        insulationThickness: Millimeters? = null,
        length: Double = 100.0,
        width: Double = 12.0,
        wallHeight: Double = 3.0,
    ) = HouseDimensions(
        id = "id-1",
        projectId = "p-1",
        length = Meters(length),
        width = Meters(width),
        wallHeight = Meters(wallHeight),
        roofType = roofType,
        ridgeHeight = ridgeHeight,
        wallMaterial = WallMaterial.BLOCK,
        floorType = FloorType.CONCRETE,
        insulationType = insulationType,
        insulationThickness = insulationThickness,
        orientation = HouseOrientation.N,
        createdAt = 0L,
        updatedAt = 0L,
    )

    @Test
    fun invoke_givenAllValid_returnsValid() {
        val result = useCase(validDimensions())
        assertTrue(result is DimensionValidationResult.Valid)
    }

    @Test
    fun invoke_givenLengthZero_returnsMustBePositive() {
        val result = useCase(validDimensions(length = 0.0))
        assertInvalid(result, DimensionField.LENGTH, DimensionErrorCode.MUST_BE_POSITIVE)
    }

    @Test
    fun invoke_givenLengthNegative_returnsMustBePositive() {
        val result = useCase(validDimensions(length = -1.0))
        assertInvalid(result, DimensionField.LENGTH, DimensionErrorCode.MUST_BE_POSITIVE)
    }

    @Test
    fun invoke_givenLengthExceedsMax_returnsLengthExceedsMax() {
        val result = useCase(validDimensions(length = 501.0))
        assertInvalid(result, DimensionField.LENGTH, DimensionErrorCode.LENGTH_EXCEEDS_MAX)
    }

    @Test
    fun invoke_givenLengthAtMax_returnsValid() {
        val result = useCase(validDimensions(length = ValidateHouseDimensionsUseCase.LENGTH_MAX))
        assertTrue(result is DimensionValidationResult.Valid)
    }

    @Test
    fun invoke_givenWidthExceedsMax_returnsWidthExceedsMax() {
        val result = useCase(validDimensions(width = 101.0))
        assertInvalid(result, DimensionField.WIDTH, DimensionErrorCode.WIDTH_EXCEEDS_MAX)
    }

    @Test
    fun invoke_givenWallHeightExceedsMax_returnsWallHeightExceedsMax() {
        val result = useCase(validDimensions(wallHeight = 16.0))
        assertInvalid(result, DimensionField.WALL_HEIGHT, DimensionErrorCode.WALL_HEIGHT_EXCEEDS_MAX)
    }

    @Test
    fun invoke_givenPitchedWithoutRidgeHeight_returnsRequiredForPitched() {
        val result =
            useCase(
                validDimensions(roofType = RoofType.PITCHED, ridgeHeight = null),
            )
        assertInvalid(
            result,
            DimensionField.RIDGE_HEIGHT,
            DimensionErrorCode.RIDGE_HEIGHT_REQUIRED_FOR_PITCHED,
        )
    }

    @Test
    fun invoke_givenPitchedRidgeHeightExceedsMax_returnsExceedsMax() {
        val result =
            useCase(
                validDimensions(roofType = RoofType.PITCHED, ridgeHeight = Meters(21.0)),
            )
        assertInvalid(
            result,
            DimensionField.RIDGE_HEIGHT,
            DimensionErrorCode.RIDGE_HEIGHT_EXCEEDS_MAX,
        )
    }

    @Test
    fun invoke_givenFlatRoofWithRidgeHeight_returnsNotAllowed() {
        val result =
            useCase(
                validDimensions(roofType = RoofType.FLAT, ridgeHeight = Meters(5.0)),
            )
        assertInvalid(
            result,
            DimensionField.RIDGE_HEIGHT,
            DimensionErrorCode.RIDGE_HEIGHT_NOT_ALLOWED_WHEN_NOT_PITCHED,
        )
    }

    @Test
    fun invoke_givenNonNoneInsulationWithoutThickness_returnsRequired() {
        val result =
            useCase(
                validDimensions(
                    insulationType = InsulationType.POLYSTYRENE,
                    insulationThickness = null,
                ),
            )
        assertInvalid(
            result,
            DimensionField.INSULATION_THICKNESS,
            DimensionErrorCode.INSULATION_THICKNESS_REQUIRED_WHEN_NOT_NONE,
        )
    }

    @Test
    fun invoke_givenNoneInsulationWithThickness_returnsNotAllowed() {
        val result =
            useCase(
                validDimensions(
                    insulationType = InsulationType.NONE,
                    insulationThickness = Millimeters(50.0),
                ),
            )
        assertInvalid(
            result,
            DimensionField.INSULATION_THICKNESS,
            DimensionErrorCode.INSULATION_THICKNESS_NOT_ALLOWED_WHEN_NONE,
        )
    }

    @Test
    fun invoke_givenMultipleViolations_returnsAllFieldErrors() {
        val result =
            useCase(
                validDimensions(
                    length = 0.0,
                    width = 0.0,
                    roofType = RoofType.PITCHED,
                    ridgeHeight = null,
                    insulationType = InsulationType.POLYSTYRENE,
                    insulationThickness = null,
                ),
            )
        assertTrue(result is DimensionValidationResult.Invalid)
        val invalid = result as DimensionValidationResult.Invalid
        assertEquals(4, invalid.fieldErrors.size)
        assertEquals(DimensionErrorCode.MUST_BE_POSITIVE, invalid.fieldErrors[DimensionField.LENGTH])
        assertEquals(DimensionErrorCode.MUST_BE_POSITIVE, invalid.fieldErrors[DimensionField.WIDTH])
        assertEquals(
            DimensionErrorCode.RIDGE_HEIGHT_REQUIRED_FOR_PITCHED,
            invalid.fieldErrors[DimensionField.RIDGE_HEIGHT],
        )
        assertEquals(
            DimensionErrorCode.INSULATION_THICKNESS_REQUIRED_WHEN_NOT_NONE,
            invalid.fieldErrors[DimensionField.INSULATION_THICKNESS],
        )
    }

    private fun assertInvalid(
        result: com.poultry.broiler.domain.validation.DimensionValidationResult,
        field: DimensionField,
        code: DimensionErrorCode,
    ) {
        assertTrue(result is DimensionValidationResult.Invalid, "Expected Invalid but was $result")
        val invalid = result as DimensionValidationResult.Invalid
        assertEquals(code, invalid.fieldErrors[field])
    }
}
