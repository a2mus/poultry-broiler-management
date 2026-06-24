package com.poultry.broiler.domain.model

/**
 * Identifies the insulation strategy used for a broiler house.
 *
 * Selecting [NONE] hides the thickness input and clears any previously entered
 * thickness value (FR-019 symmetry with the roof-type conditional field).
 *
 * @property displayNameFr The French display label shown in the wizard selectors.
 */
enum class InsulationType(val displayNameFr: String) {
    NONE("Aucune"),
    POLYSTYRENE("Polystyrène"),
    POLYURETHANE("Polyuréthane"),
    MINERAL_WOOL("Laine minérale"),
}