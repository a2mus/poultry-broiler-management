package com.poultry.broiler.domain.model

/**
 * Identifies the primary wall construction material of a broiler house.
 *
 * Material selection impacts future ventilation and insulation calculations.
 *
 * @property displayNameFr The French display label shown in the wizard selectors.
 */
enum class WallMaterial(val displayNameFr: String) {
    BLOCK("Parpaing"),
    STEEL("Acier"),
    PREFAB("Préfabriqué"),
}