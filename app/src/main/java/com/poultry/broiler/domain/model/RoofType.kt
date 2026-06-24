package com.poultry.broiler.domain.model

/**
 * Identifies the structural roof configuration of a broiler house.
 *
 * The roof type drives conditional form fields: only [PITCHED] roofs require a
 * ridge height value (FR-019).
 *
 * @property displayNameFr The French display label shown in the wizard selectors.
 */
enum class RoofType(val displayNameFr: String) {
    FLAT("Plat"),
    PITCHED("À pignon"),
    ARCHED("Voûté"),
}