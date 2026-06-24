package com.poultry.broiler.domain.model

/**
 * Domain primitive representing a length measured in meters.
 *
 * Wraps a raw [Double] to prevent type confusion with other metric primitives
 * such as [Millimeters] or [SquareMeters] (Constitution Art 2.3).
 *
 * @property value The numeric length in meters.
 */
@JvmInline
value class Meters(val value: Double) {
    operator fun compareTo(other: Meters): Int = value.compareTo(other.value)
}

/**
 * Domain primitive representing a length or thickness measured in millimeters.
 *
 * Used for sub-meter measurements such as insulation thickness where meter
 * precision would lose meaningful detail.
 *
 * @property value The numeric length in millimeters.
 */
@JvmInline
value class Millimeters(val value: Double) {
    operator fun compareTo(other: Millimeters): Int = value.compareTo(other.value)
}

/**
 * Domain primitive representing an area measured in square meters.
 *
 * Returned by area calculations (length × width) and rendered in the UI
 * with the "m²" suffix.
 *
 * @property value The numeric area in square meters.
 */
@JvmInline
value class SquareMeters(val value: Double)