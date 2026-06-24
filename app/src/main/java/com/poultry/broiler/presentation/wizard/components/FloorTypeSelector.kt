package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Foundation
import androidx.compose.material.icons.filled.TileWork
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Segmented selector for floor type (Concrete, Dirt, Slat).
 *
 * Each option uses an illustrative icon (slab, soil, grid) plus a localized
 * label. The selected card adopts the `primaryContainer` styling and shows a
 * checkmark overlay.
 *
 * @param selectedType Currently selected floor type or `null`.
 * @param onSelect Invoked with the chosen [FloorType] when tapped.
 */
@Composable
fun FloorTypeSelector(
    selectedType: FloorType?,
    onSelect: (FloorType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        FloorCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_floor_type_concrete),
            icon = Icons.Filled.Foundation,
            selected = selectedType == FloorType.CONCRETE,
            onClick = { onSelect(FloorType.CONCRETE) },
        )
        FloorCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_floor_type_dirt),
            icon = Icons.Filled.Agriculture,
            selected = selectedType == FloorType.DIRT,
            onClick = { onSelect(FloorType.DIRT) },
        )
        FloorCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_floor_type_slat),
            icon = Icons.Filled.TileWork,
            selected = selectedType == FloorType.SLAT,
            onClick = { onSelect(FloorType.SLAT) },
        )
    }
}

@Composable
private fun FloorCard(
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