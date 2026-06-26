package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.SquareMeters
import javax.inject.Inject

/**
 * Computes the floor area of a building from its outside dimensions.
 *
 * Pure function with no I/O or state — safe to call from any dispatcher.
 * The result is rounded to two decimal places for display purposes (FR-021).
 */
class CalculateFloorAreaUseCase
    @Inject
    constructor() {
        /**
         * @param length Building length in meters.
         * @param width Building width in meters.
         * @return The floor area in square meters, rounded to two decimal places.
         */
        operator fun invoke(
            length: Meters,
            width: Meters,
        ): SquareMeters {
            val raw = length.value * width.value
            val rounded = Math.round(raw * 100.0) / 100.0
            return SquareMeters(rounded)
        }
    }
