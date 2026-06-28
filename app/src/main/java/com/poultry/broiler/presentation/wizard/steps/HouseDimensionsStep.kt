package com.poultry.broiler.presentation.wizard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.validation.DimensionField
import com.poultry.broiler.presentation.components.NumericInputField
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.wizard.components.DimensionPreviewCanvas
import com.poultry.broiler.presentation.wizard.components.FloorTypeSelector
import com.poultry.broiler.presentation.wizard.components.InsulationConfigSection
import com.poultry.broiler.presentation.wizard.components.OrientationSelector
import com.poultry.broiler.presentation.wizard.components.RoofTypeSelector
import com.poultry.broiler.presentation.wizard.components.WallMaterialSelector

/**
 * Step 1 of the wizard — the House Dimensions form.
 *
 * Renders structural dimensions, roof type (with conditional ridge height),
 * and the live 2D preview. Material / floor selectors, insulation section, and
 * orientation compass are layered onto this composable across later
 * implementation phases of Feature #003.
 */
@Composable
fun HouseDimensionsStep(
    formState: DimensionsFormState,
    onIntent: (WizardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = spacing.md,
                    end = spacing.md,
                    top = spacing.md,
                    bottom = spacing.xxl,
                ),
    ) {
        val isWide = maxWidth >= 600.dp

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.md),
                ) {
                    SectionCard(title = stringResource(R.string.wizard_section_structure)) {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                            NumericInputField(
                                value = formState.length,
                                onValueChange = { onIntent(WizardIntent.UpdateLength(it)) },
                                label = stringResource(R.string.wizard_field_length),
                                unitLabel = stringResource(R.string.wizard_unit_meters),
                                errorMessage = formState.fieldErrors[DimensionField.LENGTH],
                            )
                            NumericInputField(
                                value = formState.width,
                                onValueChange = { onIntent(WizardIntent.UpdateWidth(it)) },
                                label = stringResource(R.string.wizard_field_width),
                                unitLabel = stringResource(R.string.wizard_unit_meters),
                                errorMessage = formState.fieldErrors[DimensionField.WIDTH],
                            )
                            NumericInputField(
                                value = formState.wallHeight,
                                onValueChange = { onIntent(WizardIntent.UpdateWallHeight(it)) },
                                label = stringResource(R.string.wizard_field_wall_height),
                                unitLabel = stringResource(R.string.wizard_unit_meters),
                                errorMessage = formState.fieldErrors[DimensionField.WALL_HEIGHT],
                            )
                            FloorAreaRow(formState)
                        }
                    }

                    SectionCard(title = stringResource(R.string.wizard_section_preview)) {
                        DimensionPreviewCanvas(
                            length = formState.length.toDoubleOrNull(),
                            width = formState.width.toDoubleOrNull(),
                            orientation = formState.orientation,
                        )
                    }

                    SectionCard(title = stringResource(R.string.wizard_section_orientation)) {
                        OrientationSelector(
                            selectedOrientation = formState.orientation,
                            onSelect = { onIntent(WizardIntent.SelectOrientation(it)) },
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.md),
                ) {
                    SectionCard(title = stringResource(R.string.wizard_section_roof)) {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                            RoofTypeSelector(
                                selectedType = formState.roofType,
                                onSelect = { onIntent(WizardIntent.SelectRoofType(it)) },
                            )
                            AnimatedVisibility(
                                visible = formState.roofType == RoofType.PITCHED,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                NumericInputField(
                                    value = formState.ridgeHeight,
                                    onValueChange = { onIntent(WizardIntent.UpdateRidgeHeight(it)) },
                                    label = stringResource(R.string.wizard_field_ridge_height),
                                    unitLabel = stringResource(R.string.wizard_unit_meters),
                                    errorMessage = formState.fieldErrors[DimensionField.RIDGE_HEIGHT],
                                )
                            }
                        }
                    }

                    SectionCard(title = stringResource(R.string.wizard_section_walls)) {
                        WallMaterialSelector(
                            selectedMaterial = formState.wallMaterial,
                            onSelect = { onIntent(WizardIntent.SelectWallMaterial(it)) },
                        )
                    }

                    SectionCard(title = stringResource(R.string.wizard_section_floor)) {
                        FloorTypeSelector(
                            selectedType = formState.floorType,
                            onSelect = { onIntent(WizardIntent.SelectFloorType(it)) },
                        )
                    }

                    SectionCard(title = stringResource(R.string.wizard_section_insulation)) {
                        InsulationConfigSection(
                            selectedType = formState.insulationType,
                            onSelectType = { onIntent(WizardIntent.SelectInsulationType(it)) },
                            thickness = formState.insulationThickness,
                            onThicknessChange = { onIntent(WizardIntent.UpdateInsulationThickness(it)) },
                            thicknessError = formState.fieldErrors[DimensionField.INSULATION_THICKNESS],
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                SectionCard(title = stringResource(R.string.wizard_section_structure)) {
                    Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                        NumericInputField(
                            value = formState.length,
                            onValueChange = { onIntent(WizardIntent.UpdateLength(it)) },
                            label = stringResource(R.string.wizard_field_length),
                            unitLabel = stringResource(R.string.wizard_unit_meters),
                            errorMessage = formState.fieldErrors[DimensionField.LENGTH],
                        )
                        NumericInputField(
                            value = formState.width,
                            onValueChange = { onIntent(WizardIntent.UpdateWidth(it)) },
                            label = stringResource(R.string.wizard_field_width),
                            unitLabel = stringResource(R.string.wizard_unit_meters),
                            errorMessage = formState.fieldErrors[DimensionField.WIDTH],
                        )
                        NumericInputField(
                            value = formState.wallHeight,
                            onValueChange = { onIntent(WizardIntent.UpdateWallHeight(it)) },
                            label = stringResource(R.string.wizard_field_wall_height),
                            unitLabel = stringResource(R.string.wizard_unit_meters),
                            errorMessage = formState.fieldErrors[DimensionField.WALL_HEIGHT],
                        )
                        FloorAreaRow(formState)
                    }
                }

                SectionCard(title = stringResource(R.string.wizard_section_preview)) {
                    DimensionPreviewCanvas(
                        length = formState.length.toDoubleOrNull(),
                        width = formState.width.toDoubleOrNull(),
                        orientation = formState.orientation,
                    )
                }

                SectionCard(title = stringResource(R.string.wizard_section_roof)) {
                    Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                        RoofTypeSelector(
                            selectedType = formState.roofType,
                            onSelect = { onIntent(WizardIntent.SelectRoofType(it)) },
                        )
                        AnimatedVisibility(
                            visible = formState.roofType == RoofType.PITCHED,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            NumericInputField(
                                value = formState.ridgeHeight,
                                onValueChange = { onIntent(WizardIntent.UpdateRidgeHeight(it)) },
                                label = stringResource(R.string.wizard_field_ridge_height),
                                unitLabel = stringResource(R.string.wizard_unit_meters),
                                errorMessage = formState.fieldErrors[DimensionField.RIDGE_HEIGHT],
                            )
                        }
                    }
                }

                SectionCard(title = stringResource(R.string.wizard_section_orientation)) {
                    OrientationSelector(
                        selectedOrientation = formState.orientation,
                        onSelect = { onIntent(WizardIntent.SelectOrientation(it)) },
                    )
                }

                SectionCard(title = stringResource(R.string.wizard_section_walls)) {
                    WallMaterialSelector(
                        selectedMaterial = formState.wallMaterial,
                        onSelect = { onIntent(WizardIntent.SelectWallMaterial(it)) },
                    )
                }

                SectionCard(title = stringResource(R.string.wizard_section_floor)) {
                    FloorTypeSelector(
                        selectedType = formState.floorType,
                        onSelect = { onIntent(WizardIntent.SelectFloorType(it)) },
                    )
                }

                SectionCard(title = stringResource(R.string.wizard_section_insulation)) {
                    InsulationConfigSection(
                        selectedType = formState.insulationType,
                        onSelectType = { onIntent(WizardIntent.SelectInsulationType(it)) },
                        thickness = formState.insulationThickness,
                        onThicknessChange = { onIntent(WizardIntent.UpdateInsulationThickness(it)) },
                        thicknessError = formState.fieldErrors[DimensionField.INSULATION_THICKNESS],
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 6.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            content()
        }
    }
}

@Composable
private fun FloorAreaRow(form: DimensionsFormState) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.wizard_field_floor_area),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text =
                form.floorArea?.let {
                    stringResource(R.string.wizard_floor_area_format, it.value)
                } ?: stringResource(R.string.wizard_unit_square_meters),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = spacing.sm),
        )
    }
}
