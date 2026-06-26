package com.poultry.broiler.presentation.wizard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.Millimeters
import com.poultry.broiler.domain.model.SquareMeters
import com.poultry.broiler.domain.usecase.CalculateFloorAreaUseCase
import com.poultry.broiler.domain.usecase.GetHouseDimensionsUseCase
import com.poultry.broiler.domain.usecase.SaveHouseDimensionsUseCase
import com.poultry.broiler.domain.usecase.ValidateHouseDimensionsUseCase
import com.poultry.broiler.domain.validation.DimensionErrorCode
import com.poultry.broiler.domain.validation.DimensionField
import com.poultry.broiler.domain.validation.DimensionValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private const val WIZARD_TOTAL_STEPS = 6
private const val STEP_HOUSE_DIMENSIONS = 1

/**
 * Orchestrates the House Dimensions wizard step (Feature #003).
 *
 * Follows the UDF pattern mandated by Constitution Art 1.2.4 — the UI sends
 * [WizardIntent]s; this ViewModel translates them into state updates emitted
 * through [uiState]. Dual-layer validation (Art 4.3) is enforced by recomputing
 * [DimensionsFormState.fieldErrors] after every intent via
 * [ValidateHouseDimensionsUseCase]; persistence ("save on every field change",
 * research.md §4) only fires when the form is fully valid to avoid spamming the
 * save-error Snackbar.
 */
@HiltViewModel
class WizardViewModel
    @Inject
    constructor(
        @dagger.hilt.android.qualifiers.ApplicationContext private val appContext: Context,
        private val getHouseDimensionsUseCase: GetHouseDimensionsUseCase,
        private val saveHouseDimensionsUseCase: SaveHouseDimensionsUseCase,
        private val calculateFloorAreaUseCase: CalculateFloorAreaUseCase,
        private val validateHouseDimensionsUseCase: ValidateHouseDimensionsUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<WizardUiState>(WizardUiState.Loading)
        val uiState: StateFlow<WizardUiState> = _uiState.asStateFlow()

        /** Id of the dimensions record once it has been created/loaded. */
        private var dimensionsId: String? = null

        /** Sticky flag set after the initial restore completes so we don't overwrite changes. */
        private var loaded = false

        /**
         * Loads the persisted dimensions for [projectId] and seeds the form.
         *
         * Safe to call once per ViewModel instance. Subsequent calls during the
         * same lifecycle are no-ops (the form may already be dirty).
         */
        fun load(projectId: String) {
            if (loaded) return
            loaded = true
            viewModelScope.launch {
                try {
                    val existing = getHouseDimensionsUseCase(projectId).first()
                    dimensionsId = existing?.id ?: UUID.randomUUID().toString()
                    val formState =
                        if (existing != null) {
                            DimensionsFormState(
                                length = existing.length.value.toString(),
                                width = existing.width.value.toString(),
                                wallHeight = existing.wallHeight.value.toString(),
                                roofType = existing.roofType,
                                ridgeHeight = existing.ridgeHeight?.value?.toString().orEmpty(),
                                wallMaterial = existing.wallMaterial,
                                floorType = existing.floorType,
                                insulationType = existing.insulationType,
                                insulationThickness = existing.insulationThickness?.value?.toString().orEmpty(),
                                orientation = existing.orientation,
                                floorArea = calculateFloorAreaFrom(existing.length.value, existing.width.value),
                                fieldErrors = emptyMap(),
                            )
                        } else {
                            DimensionsFormState()
                        }
                    _uiState.value =
                        WizardUiState.Active(
                            currentStep = STEP_HOUSE_DIMENSIONS,
                            totalSteps = WIZARD_TOTAL_STEPS,
                            dimensions = formState,
                            canGoNext = computeCanGoNext(formState, formState.fieldErrors),
                            canGoPrevious = false,
                            saveError = null,
                        )
                } catch (t: Throwable) {
                    _uiState.value = WizardUiState.Error(t.message ?: "Erreur de chargement")
                }
            }
        }

        /**
         * Dispatches a [WizardIntent] to mutate the wizard state.
         *
         * The dispatching is single-threaded (viewModelScope) so concurrent writes
         * to [_uiState] are impossible; we simply recompute the [DimensionsFormState]
         * for each intent and trigger persistence when the new state differs.
         */
        fun onIntent(
            intent: WizardIntent,
            projectId: String,
        ) {
            val active = _uiState.value as? WizardUiState.Active ?: return
            val updatedForm =
                when (intent) {
                    is WizardIntent.UpdateLength ->
                        active.dimensions.copy(
                            length = intent.value,
                            floorArea = calculateFloorAreaOrNull(intent.value, active.dimensions.width),
                        )
                    is WizardIntent.UpdateWidth ->
                        active.dimensions.copy(
                            width = intent.value,
                            floorArea = calculateFloorAreaOrNull(active.dimensions.length, intent.value),
                        )
                    is WizardIntent.UpdateWallHeight ->
                        active.dimensions.copy(
                            wallHeight = intent.value,
                        )
                    is WizardIntent.UpdateRidgeHeight ->
                        active.dimensions.copy(
                            ridgeHeight = intent.value,
                        )
                    is WizardIntent.SelectRoofType ->
                        active.dimensions.copy(
                            roofType = intent.type,
                            ridgeHeight =
                                if (intent.type == com.poultry.broiler.domain.model.RoofType.PITCHED) {
                                    active.dimensions.ridgeHeight
                                } else {
                                    ""
                                },
                        )
                    is WizardIntent.SelectWallMaterial ->
                        active.dimensions.copy(
                            wallMaterial = intent.material,
                        )
                    is WizardIntent.SelectFloorType ->
                        active.dimensions.copy(
                            floorType = intent.type,
                        )
                    is WizardIntent.SelectInsulationType ->
                        active.dimensions.copy(
                            insulationType = intent.type,
                            insulationThickness =
                                if (intent.type == com.poultry.broiler.domain.model.InsulationType.NONE) {
                                    ""
                                } else {
                                    active.dimensions.insulationThickness
                                },
                        )
                    is WizardIntent.UpdateInsulationThickness ->
                        active.dimensions.copy(
                            insulationThickness = intent.value,
                        )
                    is WizardIntent.SelectOrientation ->
                        active.dimensions.copy(
                            orientation = intent.orientation,
                        )
                    WizardIntent.GoNext -> active.dimensions
                    WizardIntent.GoPrevious -> active.dimensions
                }

            // Merge Compose-layer field validation with domain-layer validation.
            val composeErrors = composeFieldErrors(intent, updatedForm)
            val domainErrors = domainFieldErrors(updatedForm)
            val fieldErrors: Map<DimensionField, String> =
                (composeErrors + domainErrors)
                    .mapValues { it.value }

            val stepAdjustedForm: DimensionsFormState = updatedForm
            val canAdvance =
                when (intent) {
                    WizardIntent.GoNext -> computeCanGoNext(stepAdjustedForm, fieldErrors)
                    WizardIntent.GoPrevious -> true
                    else -> false
                }
            val targetStep =
                when (intent) {
                    WizardIntent.GoNext ->
                        if (canAdvance) {
                            (active.currentStep + 1).coerceAtMost(active.totalSteps)
                        } else {
                            active.currentStep
                        }
                    WizardIntent.GoPrevious -> (active.currentStep - 1).coerceAtLeast(STEP_HOUSE_DIMENSIONS)
                    else -> active.currentStep
                }

            val nextActive =
                active.copy(
                    currentStep = targetStep,
                    dimensions = stepAdjustedForm.copy(fieldErrors = fieldErrors),
                    canGoNext = computeCanGoNext(stepAdjustedForm, fieldErrors),
                    canGoPrevious = targetStep > STEP_HOUSE_DIMENSIONS,
                )
            _uiState.value = nextActive

            if (intent !is WizardIntent.GoNext && intent !is WizardIntent.GoPrevious) {
                if (fieldErrors.isEmpty()) {
                    autoSave(nextActive.dimensions, projectId)
                }
            }
        }

        /**
         * Compose-layer immediate feedback: combined required-field check plus
         * simple range tests executed on each keystroke (Constitution Art 4.3
         * layer 1).
         */
        private fun composeFieldErrors(
            intent: WizardIntent,
            form: DimensionsFormState,
        ): Map<DimensionField, String> {
            val errors = mutableMapOf<DimensionField, String>()
            when (intent) {
                is WizardIntent.UpdateLength ->
                    checkRange(
                        form.length,
                        DimensionField.LENGTH,
                        maxValue = ValidateHouseDimensionsUseCase.LENGTH_MAX,
                        exceedsResId = R.string.wizard_error_length_exceeds_max,
                        errors = errors,
                    )
                is WizardIntent.UpdateWidth ->
                    checkRange(
                        form.width,
                        DimensionField.WIDTH,
                        maxValue = ValidateHouseDimensionsUseCase.WIDTH_MAX,
                        exceedsResId = R.string.wizard_error_width_exceeds_max,
                        errors = errors,
                    )
                is WizardIntent.UpdateWallHeight ->
                    checkRange(
                        form.wallHeight,
                        DimensionField.WALL_HEIGHT,
                        maxValue = ValidateHouseDimensionsUseCase.WALL_HEIGHT_MAX,
                        exceedsResId = R.string.wizard_error_wall_height_exceeds_max,
                        errors = errors,
                    )
                is WizardIntent.UpdateRidgeHeight -> {
                    if (form.roofType == com.poultry.broiler.domain.model.RoofType.PITCHED &&
                        form.ridgeHeight.isNotEmpty()
                    ) {
                        val v = form.ridgeHeight.toDoubleOrNull()
                        if (v != null && v > ValidateHouseDimensionsUseCase.RIDGE_HEIGHT_MAX) {
                            errors[DimensionField.RIDGE_HEIGHT] =
                                getString(R.string.wizard_error_ridge_height_exceeds_max)
                        } else if (v == null) {
                            errors[DimensionField.RIDGE_HEIGHT] =
                                getString(R.string.wizard_error_value_must_be_positive)
                        }
                    }
                }
                is WizardIntent.UpdateInsulationThickness -> {
                    if (form.insulationType != com.poultry.broiler.domain.model.InsulationType.NONE &&
                        form.insulationThickness.isNotEmpty()
                    ) {
                        val v = form.insulationThickness.toDoubleOrNull()
                        if (v != null && v > ValidateHouseDimensionsUseCase.INSULATION_THICKNESS_MAX) {
                            errors[DimensionField.INSULATION_THICKNESS] =
                                getString(R.string.wizard_error_insulation_thickness_exceeds_max)
                        } else if (v == null) {
                            errors[DimensionField.INSULATION_THICKNESS] =
                                getString(R.string.wizard_error_value_must_be_positive)
                        }
                    }
                }
                else -> Unit
            }
            return errors
        }

        private fun checkRange(
            raw: String,
            field: DimensionField,
            maxValue: Double,
            exceedsResId: Int,
            errors: MutableMap<DimensionField, String>,
        ) {
            if (raw.isEmpty()) return
            val v = raw.toDoubleOrNull()
            if (v == null) {
                errors[field] = getString(R.string.wizard_error_value_must_be_positive)
            } else if (v <= 0.0) {
                errors[field] = getString(R.string.wizard_error_value_must_be_positive)
            } else if (v > maxValue) {
                errors[field] = getString(exceedsResId)
            }
        }

        /**
         * Domain-layer validation: builds a [HouseDimensions] if possible and runs
         * [ValidateHouseDimensionsUseCase]. Fields that aren't yet set fall back to
         * REQUIRED messages.
         */
        private fun domainFieldErrors(form: DimensionsFormState): Map<DimensionField, String> {
            val errors = mutableMapOf<DimensionField, String>()
            val length =
                form.length.toDoubleOrNull()
                    ?: run {
                        errors[DimensionField.LENGTH] = getString(R.string.wizard_error_length_required)
                        null
                    }
            val width =
                form.width.toDoubleOrNull()
                    ?: run {
                        errors[DimensionField.WIDTH] = getString(R.string.wizard_error_width_required)
                        null
                    }
            val wallHeight =
                form.wallHeight.toDoubleOrNull()
                    ?: run {
                        errors[DimensionField.WALL_HEIGHT] = getString(R.string.wizard_error_wall_height_required)
                        null
                    }
            val roofType =
                form.roofType
                    ?: run {
                        errors[DimensionField.ROOF_TYPE] = getString(R.string.wizard_error_roof_type_required)
                        null
                    }
            val wallMaterial =
                form.wallMaterial
                    ?: run {
                        errors[DimensionField.WALL_MATERIAL] = getString(R.string.wizard_error_wall_material_required)
                        null
                    }
            val floorType =
                form.floorType
                    ?: run {
                        errors[DimensionField.FLOOR_TYPE] = getString(R.string.wizard_error_floor_type_required)
                        null
                    }
            val insulationType =
                form.insulationType
                    ?: run {
                        errors[DimensionField.INSULATION_TYPE] =
                            getString(R.string.wizard_error_insulation_type_required)
                        null
                    }
            val orientation =
                form.orientation
                    ?: run {
                        errors[DimensionField.ORIENTATION] = getString(R.string.wizard_error_orientation_required)
                        null
                    }

            if (length == null || width == null || wallHeight == null || roofType == null ||
                wallMaterial == null || floorType == null || insulationType == null || orientation == null
            ) {
                // The REQUIRED messages already populated above; skip domain-level checks.
                return errors
            }

            val ridgeHeight = form.ridgeHeight.toDoubleOrNull()?.let(::Meters)
            val insulationThickness =
                form.insulationThickness.toDoubleOrNull()?.let(
                    ::Millimeters,
                )
            val dimension =
                HouseDimensions(
                    id = dimensionsId ?: UUID.randomUUID().toString(),
                    projectId = "",
                    length = Meters(length),
                    width = Meters(width),
                    wallHeight = Meters(wallHeight),
                    roofType = roofType,
                    ridgeHeight = ridgeHeight,
                    wallMaterial = wallMaterial,
                    floorType = floorType,
                    insulationType = insulationType,
                    insulationThickness = insulationThickness,
                    orientation = orientation,
                    createdAt = 0L,
                    updatedAt = 0L,
                )
            when (val result = validateHouseDimensionsUseCase(dimension)) {
                DimensionValidationResult.Valid -> Unit
                is DimensionValidationResult.Invalid ->
                    result.fieldErrors.forEach { (field, code) ->
                        errors[field] = localize(code)
                    }
            }
            return errors
        }

        private fun localize(code: DimensionErrorCode): String =
            when (code) {
                DimensionErrorCode.REQUIRED -> getString(R.string.wizard_error_value_must_be_positive)
                DimensionErrorCode.MUST_BE_POSITIVE -> getString(R.string.wizard_error_value_must_be_positive)
                DimensionErrorCode.LENGTH_EXCEEDS_MAX -> getString(R.string.wizard_error_length_exceeds_max)
                DimensionErrorCode.WIDTH_EXCEEDS_MAX -> getString(R.string.wizard_error_width_exceeds_max)
                DimensionErrorCode.WALL_HEIGHT_EXCEEDS_MAX -> getString(R.string.wizard_error_wall_height_exceeds_max)
                DimensionErrorCode.RIDGE_HEIGHT_EXCEEDS_MAX -> getString(R.string.wizard_error_ridge_height_exceeds_max)
                DimensionErrorCode.RIDGE_HEIGHT_REQUIRED_FOR_PITCHED ->
                    getString(R.string.wizard_error_ridge_height_required)
                DimensionErrorCode.RIDGE_HEIGHT_NOT_ALLOWED_WHEN_NOT_PITCHED ->
                    getString(R.string.wizard_error_ridge_height_not_allowed)
                DimensionErrorCode.INSULATION_THICKNESS_REQUIRED_WHEN_NOT_NONE ->
                    getString(R.string.wizard_error_insulation_thickness_required)
                DimensionErrorCode.INSULATION_THICKNESS_NOT_ALLOWED_WHEN_NONE ->
                    getString(R.string.wizard_error_insulation_thickness_not_allowed)
            }

        private fun getString(resId: Int): String = appContext.getString(resId)

        /**
         * Next becomes enabled only when every required field is non-empty AND the
         * merged validation result has no entry — i.e. all field errors clear.
         */
        private fun computeCanGoNext(
            form: DimensionsFormState,
            fieldErrors: Map<DimensionField, String>,
        ): Boolean {
            val basicFilled =
                form.length.toDoubleOrNull() != null &&
                    form.width.toDoubleOrNull() != null &&
                    form.wallHeight.toDoubleOrNull() != null &&
                    form.roofType != null &&
                    form.wallMaterial != null &&
                    form.floorType != null &&
                    form.insulationType != null &&
                    form.orientation != null &&
                    ridgeRequiredSatisfied(form) &&
                    thicknessRequiredSatisfied(form)
            if (!basicFilled) return false
            return fieldErrors.isEmpty()
        }

        private fun ridgeRequiredSatisfied(form: DimensionsFormState): Boolean =
            when (form.roofType) {
                com.poultry.broiler.domain.model.RoofType.PITCHED ->
                    form.ridgeHeight.toDoubleOrNull() != null
                null -> true
                else -> true
            }

        private fun thicknessRequiredSatisfied(form: DimensionsFormState): Boolean =
            when (form.insulationType) {
                com.poultry.broiler.domain.model.InsulationType.NONE -> true
                null -> true
                else -> form.insulationThickness.toDoubleOrNull() != null
            }

        private fun calculateFloorAreaOrNull(
            lengthText: String,
            widthText: String,
        ): SquareMeters? {
            val l = lengthText.toDoubleOrNull() ?: return null
            val w = widthText.toDoubleOrNull() ?: return null
            return calculateFloorAreaUseCase(Meters(l), Meters(w))
        }

        private fun calculateFloorAreaFrom(
            length: Double,
            width: Double,
        ): SquareMeters = calculateFloorAreaUseCase(Meters(length), Meters(width))

        private fun autoSave(
            form: DimensionsFormState,
            projectId: String,
        ) {
            val id = dimensionsId ?: return
            val length = form.length.toDoubleOrNull() ?: return
            val width = form.width.toDoubleOrNull() ?: return
            val wallHeight = form.wallHeight.toDoubleOrNull() ?: return
            val roofType = form.roofType ?: return
            val wallMaterial = form.wallMaterial ?: return
            val floorType = form.floorType ?: return
            val insulationType = form.insulationType ?: return
            val orientation = form.orientation ?: return

            val dimensions =
                HouseDimensions(
                    id = id,
                    projectId = projectId,
                    length = Meters(length),
                    width = Meters(width),
                    wallHeight = Meters(wallHeight),
                    roofType = roofType,
                    ridgeHeight = form.ridgeHeight.toDoubleOrNull()?.let(::Meters),
                    wallMaterial = wallMaterial,
                    floorType = floorType,
                    insulationType = insulationType,
                    insulationThickness =
                        form.insulationThickness.toDoubleOrNull()?.let(
                            ::Millimeters,
                        ),
                    orientation = orientation,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                )

            viewModelScope.launch {
                saveHouseDimensionsUseCase(dimensions)
                    .onFailure { error ->
                        _uiState.update { current ->
                            if (current is WizardUiState.Active) {
                                current.copy(saveError = error.message)
                            } else {
                                current
                            }
                        }
                    }
                    .onSuccess {
                        _uiState.update { current ->
                            if (current is WizardUiState.Active && current.saveError != null) {
                                current.copy(saveError = null)
                            } else {
                                current
                            }
                        }
                    }
            }
        }
    }
