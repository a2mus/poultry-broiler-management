package com.poultry.broiler.domain.model

/**
 * The physical structure configuration of a single broiler house.
 *
 * One [HouseDimensions] record belongs to exactly one [Project] (1:1
 * relationship persisted via the `house_dimensions` table). All length fields
 * use the [Meters] primitive except for insulation thickness, which is measured
 * in [Millimeters] for sub-meter precision.
 *
 * Validation rules enforced by `ValidateHouseDimensionsUseCase`:
 * - length:           0 < value ≤ 500
 * - width:            0 < value ≤ 100
 * - wallHeight:       0 < value ≤ 15
 * - ridgeHeight:      required when [roofType] == [RoofType.PITCHED] (0 < value ≤ 20), null otherwise
 * - insulationThickness: required when [insulationType] != [InsulationType.NONE]
 *                        (0 < value ≤ 500 mm), null otherwise
 *
 * @property id Unique identifier (UUID string).
 * @property projectId Foreign key referencing the owning [Project].
 * @property length Building length along the primary axis.
 * @property width Building width perpendicular to the length axis.
 * @property wallHeight Vertical wall height from ground to eaves.
 * @property roofType Structural roof style; drives whether [ridgeHeight] is required.
 * @property ridgeHeight Height of the roof ridge above ground; nullable for non-pitched roofs.
 * @property wallMaterial Primary wall construction material.
 * @property floorType Primary floor construction type.
 * @property insulationType Insulation strategy; drives whether [insulationThickness] is required.
 * @property insulationThickness Thickness of the insulation in millimeters; null when not applicable.
 * @property orientation Cardinal heading of the building's long axis.
 * @property createdAt Creation timestamp in epoch milliseconds.
 * @property updatedAt Last modification timestamp in epoch milliseconds.
 */
data class HouseDimensions(
    val id: String,
    val projectId: String,
    val length: Meters,
    val width: Meters,
    val wallHeight: Meters,
    val roofType: RoofType,
    val ridgeHeight: Meters?,
    val wallMaterial: WallMaterial,
    val floorType: FloorType,
    val insulationType: InsulationType,
    val insulationThickness: Millimeters?,
    val orientation: HouseOrientation,
    val createdAt: Long,
    val updatedAt: Long,
) {
    /**
     * Computed floor area (length × width) in square meters.
     *
     * Not persisted to Room — derived from [length] and [width] whenever the
     * record is read. Rounded to two decimal places for display by
     * `CalculateFloorAreaUseCase`.
     */
    val floorArea: SquareMeters
        get() = SquareMeters(length.value * width.value)
}
