package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Segmented selector for the three roof types (Flat / Pitched / Arched).
 *
 * Visual:
 * - horizontal Row of three [SelectableCard]s, equal weight
 * - selected card uses `primaryContainer` + checkmark overlay
 * - each card exposes a localized `contentDescription`
 *
 * Selecting [RoofType.PITCHED] in the parent ViewModel reveals the ridge
 * height input via `AnimatedVisibility` (FR-019).
 *
 * @param selectedType Currently selected roof type or `null` when none is set.
 * @param onSelect Invoked with the chosen [RoofType] when a card is tapped.
 */
@Composable
fun RoofTypeSelector(
    selectedType: RoofType?,
    onSelect: (RoofType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        SelectableCardRow(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_roof_type_flat),
            icon = Icons.Filled.HorizontalRule,
            selected = selectedType == RoofType.FLAT,
            onClick = { onSelect(RoofType.FLAT) },
        )
        SelectableCardRow(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_roof_type_pitched),
            icon = Icons.Filled.ChangeHistory,
            selected = selectedType == RoofType.PITCHED,
            onClick = { onSelect(RoofType.PITCHED) },
        )
        SelectableCardRow(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_roof_type_arched),
            icon = Icons.Filled.Warehouse,
            selected = selectedType == RoofType.ARCHED,
            onClick = { onSelect(RoofType.ARCHED) },
        )
    }
}

@Composable
private fun SelectableCardRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        SelectableCard(
            label = label,
            selected = selected,
            icon = icon,
            onClick = onClick,
            contentDescription = label,
        )
    }
}
