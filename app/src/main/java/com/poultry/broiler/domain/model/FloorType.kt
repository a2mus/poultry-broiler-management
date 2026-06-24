package com.poultry.broiler.domain.model

/**
 * Identifies the floor construction type of a broiler house.
 *
 * Floor type affects waste management, bedding requirements, and hygiene plans.
 *
 * @property displayNameFr The French display label shown in the wizard selectors.
 */
enum class FloorType(val displayNameFr: String) {
    CONCRETE("Béton"),
    DIRT("Terre"),
    SLAT("Caillebotis"),
}