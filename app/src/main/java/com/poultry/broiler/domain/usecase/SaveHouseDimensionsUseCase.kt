package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.repository.HouseDimensionsRepository
import com.poultry.broiler.domain.validation.DimensionErrorCode
import com.poultry.broiler.domain.validation.DimensionField
import com.poultry.broiler.domain.validation.DimensionValidationResult
import java.lang.System.currentTimeMillis
import javax.inject.Inject

/**
 * Persists a [HouseDimensions] record for the wizard step.
 *
 * Validates [dimensions] against the domain rules before persisting; if any
 * rule fails, the use case returns `Result.failure` with the
 * [DimensionValidationResult.Invalid] map attached (Constitution Art 4.3). On
 * success the record is upserted and `updatedAt` is stamped.
 */
class SaveHouseDimensionsUseCase
    @Inject
    constructor(
        private val repository: HouseDimensionsRepository,
        private val validateHouseDimensionsUseCase: ValidateHouseDimensionsUseCase,
    ) {
        /**
         * @param dimensions The dimensions record to upsert.
         * @return `Result.success` with the persisted dimensions, or `Result.failure`
         *         carrying the [DimensionValidationResult.Invalid] map when
         *         validation fails (or persistence throws).
         */
        suspend operator fun invoke(dimensions: HouseDimensions): Result<HouseDimensions> {
            val validation = validateHouseDimensionsUseCase(dimensions)
            if (validation is DimensionValidationResult.Invalid) {
                return Result.failure(
                    DimensionValidationFailure(validation.fieldErrors),
                )
            }
            return runCatching {
                val stamped = dimensions.copy(updatedAt = currentTimeMillis())
                repository.saveDimensions(stamped)
                stamped
            }
        }
    }

/**
 * Domain failure type carrying field-level validation errors back to the
 * presentation layer.
 *
 * Carries the same [DimensionValidationResult.Invalid.fieldErrors] map emitted
 * by [ValidateHouseDimensionsUseCase] so the ViewModel can resolve field
 * errors without re-running validation.
 */
class DimensionValidationFailure(
    val fieldErrors: Map<DimensionField, DimensionErrorCode>,
    message: String = "Validation failed",
) : Throwable(message)
