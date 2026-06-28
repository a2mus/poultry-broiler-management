package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.Millimeters
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.validation.DimensionErrorCode
import com.poultry.broiler.domain.validation.DimensionField
import com.poultry.broiler.domain.validation.DimensionValidationResult
import javax.inject.Inject

/**
 * Pure-Kotlin validation of a [HouseDimensions] record (Constitution Art 4.3 —
 * dual-layer validation, Domain layer).
 *
 * Rules enforced:
 * - length:           0 < value ≤ 500
 * - width:            0 < value ≤ 100
 * - wallHeight:       0 < value ≤ 15
 * - ridgeHeight (PITCHED): 0 < value ≤ 20; required (non-null)
 * - ridgeHeight (non-PITCHED): must be null
 * - insulationThickness (not NONE): 0 < value ≤ 500 mm; required
 * - insulationThickness (NONE):     must be null
 *
 * Enum fields are non-null by the type system; the use case still returns
 * `REQUIRED` keys for completeness if any are null defensively.
 */
class ValidateHouseDimensionsUseCase
    @Inject
    constructor() {
        /**
         * Validates [dimensions] and returns the result.
         *
         * Always returns [DimensionValidationResult.Valid] when all rules pass,
         * otherwise returns [DimensionValidationResult.Invalid] with one entry
         * per failing rule.
         */
        operator fun invoke(dimensions: HouseDimensions): DimensionValidationResult {
            val errors = mutableMapOf<DimensionField, DimensionErrorCode>()

            validateMeters(
                value = dimensions.length.value,
                maxValue = LENGTH_MAX,
                field = DimensionField.LENGTH,
                exceedsMaxCode = DimensionErrorCode.LENGTH_EXCEEDS_MAX,
                errors = errors,
            )
            validateMeters(
                value = dimensions.width.value,
                maxValue = WIDTH_MAX,
                field = DimensionField.WIDTH,
                exceedsMaxCode = DimensionErrorCode.WIDTH_EXCEEDS_MAX,
                errors = errors,
            )
            validateMeters(
                value = dimensions.wallHeight.value,
                maxValue = WALL_HEIGHT_MAX,
                field = DimensionField.WALL_HEIGHT,
                exceedsMaxCode = DimensionErrorCode.WALL_HEIGHT_EXCEEDS_MAX,
                errors = errors,
            )
            validateRidgeHeight(
                roofType = dimensions.roofType,
                ridgeHeight = dimensions.ridgeHeight,
                errors = errors,
            )
            validateInsulationThickness(
                insulationType = dimensions.insulationType,
                thickness = dimensions.insulationThickness,
                errors = errors,
            )

            return if (errors.isEmpty()) {
                DimensionValidationResult.Valid
            } else {
                DimensionValidationResult.Invalid(fieldErrors = errors.toMap())
            }
        }

        private fun validateMeters(
            value: Double,
            maxValue: Double,
            field: DimensionField,
            exceedsMaxCode: DimensionErrorCode,
            errors: MutableMap<DimensionField, DimensionErrorCode>,
        ) {
            if (value <= 0.0) {
                errors[field] = DimensionErrorCode.MUST_BE_POSITIVE
                return
            }
            if (value > maxValue) {
                errors[field] = exceedsMaxCode
            }
        }

        private fun validateRidgeHeight(
            roofType: RoofType,
            ridgeHeight: Meters?,
            errors: MutableMap<DimensionField, DimensionErrorCode>,
        ) {
            when (roofType) {
                RoofType.PITCHED -> {
                    val v = ridgeHeight?.value
                    if (v == null) {
                        errors[DimensionField.RIDGE_HEIGHT] =
                            DimensionErrorCode.RIDGE_HEIGHT_REQUIRED_FOR_PITCHED
                    } else if (v <= 0.0) {
                        errors[DimensionField.RIDGE_HEIGHT] = DimensionErrorCode.MUST_BE_POSITIVE
                    } else if (v > RIDGE_HEIGHT_MAX) {
                        errors[DimensionField.RIDGE_HEIGHT] =
                            DimensionErrorCode.RIDGE_HEIGHT_EXCEEDS_MAX
                    }
                }
                else -> {
                    if (ridgeHeight != null) {
                        errors[DimensionField.RIDGE_HEIGHT] =
                            DimensionErrorCode.RIDGE_HEIGHT_NOT_ALLOWED_WHEN_NOT_PITCHED
                    }
                }
            }
        }

        private fun validateInsulationThickness(
            insulationType: InsulationType,
            thickness: Millimeters?,
            errors: MutableMap<DimensionField, DimensionErrorCode>,
        ) {
            when (insulationType) {
                InsulationType.NONE -> {
                    if (thickness != null) {
                        errors[DimensionField.INSULATION_THICKNESS] =
                            DimensionErrorCode.INSULATION_THICKNESS_NOT_ALLOWED_WHEN_NONE
                    }
                }
                else -> {
                    val v = thickness?.value
                    if (v == null) {
                        errors[DimensionField.INSULATION_THICKNESS] =
                            DimensionErrorCode.INSULATION_THICKNESS_REQUIRED_WHEN_NOT_NONE
                    } else if (v <= 0.0) {
                        errors[DimensionField.INSULATION_THICKNESS] = DimensionErrorCode.MUST_BE_POSITIVE
                    } else if (v > INSULATION_THICKNESS_MAX) {
                        errors[DimensionField.INSULATION_THICKNESS] =
                            DimensionErrorCode.LENGTH_EXCEEDS_MAX
                    }
                }
            }
        }

        companion object {
            const val LENGTH_MAX = 500.0
            const val WIDTH_MAX = 100.0
            const val WALL_HEIGHT_MAX = 15.0
            const val RIDGE_HEIGHT_MAX = 20.0
            const val INSULATION_THICKNESS_MAX = 500.0
        }
    }
