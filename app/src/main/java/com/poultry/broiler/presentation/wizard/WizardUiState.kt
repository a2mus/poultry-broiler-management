package com.poultry.broiler.presentation.wizard

import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.SquareMeters
import com.poultry.broiler.domain.model.WallMaterial
import com.poultry.broiler.domain.validation.DimensionField

/**
 * Sealed view-state for the House Dimensions wizard.
 *
 * States form a strict lifecycle: [Loading] → [Active] (or [Error] when the
 * initial read fails). The wizard never returns to [Loading] from [Active] —
 * subsequent writes are reflected by updating the [Active] instance.
 *
 * Constitution Art 2.3 requires sealed interfaces for finite state sets.
 */
sealed interface WizardUiState {
    /** Initial state while the persisted dimensions for the project are being read. */
    data object Loading : WizardUiState

    /** The wizard is interactive and bound to an editable dimensions form. */
    data class Active(
        /** 1-based current step index (Step 1 is the house dimensions step). */
        val currentStep: Int,
        /** Total wizard step count — currently 6. */
        val totalSteps: Int,
        /** Editable form state. */
        val dimensions: DimensionsFormState,
        /** True when all required fields are filled and valid (Next gate). */
        val canGoNext: Boolean,
        /** False on Step 1 (Previous is hidden), true thereafter. */
        val canGoPrevious: Boolean,
        /** Localized persistence error message to surface via Snackbar, or null. */
        val saveError: String?,
    ) : WizardUiState

    /** Fatal load failure that prevents rendering the wizard form. */
    data class Error(val message: String) : WizardUiState
}

/**
 * Editable form representation of the wizard step's fields.
 *
 * Numeric fields are kept as raw [String]s to mirror the input field state
 * (Constitution Art 4.3 dual-layer validation); they are parsed and wrapped
 * into domain primitives only when persisting or validating.
 *
 * @property fieldErrors Inline error per [DimensionField]; empty when no rule is violated.
 */
data class DimensionsFormState(
    val length: String = "",
    val width: String = "",
    val wallHeight: String = "",
    val roofType: RoofType? = null,
    val ridgeHeight: String = "",
    val wallMaterial: WallMaterial? = null,
    val floorType: FloorType? = null,
    val insulationType: InsulationType? = null,
    val insulationThickness: String = "",
    val orientation: HouseOrientation? = null,
    val floorArea: SquareMeters? = null,
    val fieldErrors: Map<DimensionField, String> = emptyMap(),
)
