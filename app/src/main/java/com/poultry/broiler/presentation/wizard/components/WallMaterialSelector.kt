package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.WallMaterial
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Segmented selector for wall material (Block, Steel, Prefab).
 *
 * Each option uses an illustrative icon (brick pattern, metal sheet, panel)
 * plus a localized label. The selected card adopts the `primaryContainer`
 * styling and shows a checkmark overlay (research.md §7).
 *
 * @param selectedMaterial Currently selected material or `null`.
 * @param onSelect Invoked with the chosen [WallMaterial] when tapped.
 */
@Composable
fun WallMaterialSelector(
    selectedMaterial: WallMaterial?,
    onSelect: (WallMaterial) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing.xs),
    ) {
        MaterialCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_wall_material_block),
            icon = Icons.Filled.ViewModule,
            selected = selectedMaterial == WallMaterial.BLOCK,
            onClick = { onSelect(WallMaterial.BLOCK) },
        )
        MaterialCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_wall_material_steel),
            icon = Icons.Filled.Construction,
            selected = selectedMaterial == WallMaterial.STEEL,
            onClick = { onSelect(WallMaterial.STEEL) },
        )
        MaterialCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.wizard_wall_material_prefab),
            icon = Icons.Filled.Layers,
            selected = selectedMaterial == WallMaterial.PREFAB,
            onClick = { onSelect(WallMaterial.PREFAB) },
        )
    }
}

@Composable
private fun MaterialCard(
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
