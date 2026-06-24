package com.poultry.broiler.presentation.wizard

import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.WallMaterial

/**
 * Sealed set of user intents sent from the wizard UI to [WizardViewModel]
 * (unidirectional data flow, Constitution Art 1.2.4).
 *
 * Numeric update intents carry raw [String] input; they are parsed inside the
 * ViewModel where the dual-layer field validation is applied.
 */
sealed interface WizardIntent {

    /** User changed the length field. */
    data class UpdateLength(val value: String) : WizardIntent

    /** User changed the width field. */
    data class UpdateWidth(val value: String) : WizardIntent

    /** User changed the wall height field. */
    data class UpdateWallHeight(val value: String) : WizardIntent

    /** User changed the ridge height field. */
    data class UpdateRidgeHeight(val value: String) : WizardIntent

    /** User selected a roof type. */
    data class SelectRoofType(val type: RoofType) : WizardIntent

    /** User selected a wall material. */
    data class SelectWallMaterial(val material: WallMaterial) : WizardIntent

    /** User selected a floor type. */
    data class SelectFloorType(val type: FloorType) : WizardIntent

    /** User selected an insulation type (NONE clears [WizardIntent.UpdateInsulationThickness]). */
    data class SelectInsulationType(val type: InsulationType) : WizardIntent

    /** User changed the insulation thickness field. */
    data class UpdateInsulationThickness(val value: String) : WizardIntent

    /** User selected a house orientation. */
    data class SelectOrientation(val orientation: HouseOrientation) : WizardIntent

    /** User tapped Next (advances the wizard step). */
    data object GoNext : WizardIntent

    /** User tapped Previous (returns to the previous step). */
    data object GoPrevious : WizardIntent
}