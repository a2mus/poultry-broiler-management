package com.poultry.broiler.presentation.wizard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.presentation.components.NumericInputField
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Composite section — insulation type selector (FilterChip-style cards) plus a
 * conditional thickness `NumericInputField` shown only when the selected type
 * is not [InsulationType.NONE] (FR-019 sibling rule).
 *
 * @param selectedType Currently selected insulation type or `null`.
 * @param onSelectType Invoked when the user taps a type card.
 * @param thickness Raw text value currently in the thickness field.
 * @param onThicknessChange Invoked on each thickness keystroke.
 * @param thicknessError Inline validation error to display (or null).
 */
@Composable
fun InsulationConfigSection(
    selectedType: InsulationType?,
    onSelectType: (InsulationType) -> Unit,
    thickness: String,
    onThicknessChange: (String) -> Unit,
    thicknessError: String?,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        InsulationTypeRow(
            selectedType = selectedType,
            onSelect = onSelectType,
        )
        AnimatedVisibility(
            visible = selectedType != null && selectedType != InsulationType.NONE,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            NumericInputField(
                value = thickness,
                onValueChange = onThicknessChange,
                label = stringResource(R.string.wizard_field_insulation_thickness),
                unitLabel = stringResource(R.string.wizard_unit_millimeters),
                errorMessage = thicknessError,
            )
        }
    }
}

@Composable
private fun InsulationTypeRow(
    selectedType: InsulationType?,
    onSelect: (InsulationType) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        InsulationChip(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_insulation_type_none),
            selected = selectedType == InsulationType.NONE,
            onClick = { onSelect(InsulationType.NONE) },
        )
        InsulationChip(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_insulation_type_polystyrene),
            selected = selectedType == InsulationType.POLYSTYRENE,
            onClick = { onSelect(InsulationType.POLYSTYRENE) },
        )
        InsulationChip(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_insulation_type_polyurethane),
            selected = selectedType == InsulationType.POLYURETHANE,
            onClick = { onSelect(InsulationType.POLYURETHANE) },
        )
        InsulationChip(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_insulation_type_mineral_wool),
            selected = selectedType == InsulationType.MINERAL_WOOL,
            onClick = { onSelect(InsulationType.MINERAL_WOOL) },
        )
    }
}

@Composable
private fun InsulationChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // The SelectableCard helper lays out its row with an icon slot; we pass
    // null because the insulation options rely on the label only.
    SelectableCard(
        modifier = modifier,
        label = label,
        selected = selected,
        icon = null,
        onClick = onClick,
        contentDescription = label,
    )
}