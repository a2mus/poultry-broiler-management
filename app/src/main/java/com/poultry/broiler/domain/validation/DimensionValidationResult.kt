package com.poultry.broiler.domain.validation

/**
 * Identifies a single field of the house-dimensions form.
 *
 * Used as the key in [DimensionValidationResult.Invalid.fieldErrors] so that
 * each invalid field maps to a single localized error message surfaced inline
 * in the UI (Constitution Art 4.3).
 */
enum class DimensionField {
    LENGTH,
    WIDTH,
    WALL_HEIGHT,
    RIDGE_HEIGHT,
    ROOF_TYPE,
    WALL_MATERIAL,
    FLOOR_TYPE,
    INSULATION_TYPE,
    INSULATION_THICKNESS,
    ORIENTATION,
}

/**
 * Outcome of validating a [com.poultry.broiler.domain.model.HouseDimensions] instance.
 *
 * The validation layer must never mutate state or throw on invalid input; it
 * must report every failing rule as a [DimensionField] → error-code entry so
 * the UI can display all errors simultaneously (dual-layer validation, Art 4.3).
 *
 * Error codes are intentionally locale-agnostic — the presentation layer
 * translates each [DimensionErrorCode] to a localized string resource so the
 * domain layer remains pure Kotlin with no Android imports (Constitution Art 7.3).
 */
sealed interface DimensionValidationResult {
    /** All fields satisfy the rules defined in the domain. */
    data object Valid : DimensionValidationResult

    /**
     * One or more fields failed validation.
     *
     * @property fieldErrors Maps each failing [DimensionField] to its
     *           [DimensionErrorCode]. Never empty.
     */
    data class Invalid(
        val fieldErrors: Map<DimensionField, DimensionErrorCode>,
    ) : DimensionValidationResult
}

/**
 * Machine-readable error codes produced by the domain validation layer.
 *
 * The presentation layer maps each entry to a localized string resource.
 */
enum class DimensionErrorCode {
    REQUIRED,
    MUST_BE_POSITIVE,
    LENGTH_EXCEEDS_MAX,
    WIDTH_EXCEEDS_MAX,
    WALL_HEIGHT_EXCEEDS_MAX,
    RIDGE_HEIGHT_EXCEEDS_MAX,
    RIDGE_HEIGHT_REQUIRED_FOR_PITCHED,
    RIDGE_HEIGHT_NOT_ALLOWED_WHEN_NOT_PITCHED,
    INSULATION_THICKNESS_REQUIRED_WHEN_NOT_NONE,
    INSULATION_THICKNESS_NOT_ALLOWED_WHEN_NONE,
}
