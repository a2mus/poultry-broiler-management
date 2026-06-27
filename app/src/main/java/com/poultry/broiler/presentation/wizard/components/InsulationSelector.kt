package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Segmented selector for insulation types (None, Polystyrene, Polyurethane, Mineral Wool).
 *
 * Renders as a 2x2 grid of [SelectableCard]s matching the design token specification.
 *
 * @param selectedType Currently selected insulation type or `null`.
 * @param onSelect Invoked when the user taps an option.
 */
@Composable
fun InsulationSelector(
    selectedType: InsulationType?,
    onSelect: (InsulationType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            SelectableCardBox(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.wizard_insulation_type_none),
                selected = selectedType == InsulationType.NONE,
                onClick = { onSelect(InsulationType.NONE) },
            )
            SelectableCardBox(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.wizard_insulation_type_polystyrene),
                selected = selectedType == InsulationType.POLYSTYRENE,
                onClick = { onSelect(InsulationType.POLYSTYRENE) },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            SelectableCardBox(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.wizard_insulation_type_polyurethane),
                selected = selectedType == InsulationType.POLYURETHANE,
                onClick = { onSelect(InsulationType.POLYURETHANE) },
            )
            SelectableCardBox(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.wizard_insulation_type_mineral_wool),
                selected = selectedType == InsulationType.MINERAL_WOOL,
                onClick = { onSelect(InsulationType.MINERAL_WOOL) },
            )
        }
    }
}

@Composable
private fun SelectableCardBox(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        SelectableCard(
            label = label,
            selected = selected,
            icon = null,
            onClick = onClick,
            contentDescription = label,
        )
    }
}
