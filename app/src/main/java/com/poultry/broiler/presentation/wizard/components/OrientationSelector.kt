package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * 8-point compass selector for house orientation.
 *
 * Implements the accessibility-friendly fallback layout described in
 * `contracts/composables.md`: two rows of 4 compass points each. Each cell is
 * a [SelectableCard] with a 48dp touch target (Constitution Art 3.3) and a
 * localized contentDescription.
 *
 * @param selectedOrientation Currently selected heading or `null`.
 * @param onSelect Invoked with the chosen [HouseOrientation] when tapped.
 */
@Composable
fun OrientationSelector(
    selectedOrientation: HouseOrientation?,
    onSelect: (HouseOrientation) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val labels: Map<HouseOrientation, Int> = mapOf(
        HouseOrientation.N to R.string.wizard_orientation_n,
        HouseOrientation.NE to R.string.wizard_orientation_ne,
        HouseOrientation.E to R.string.wizard_orientation_e,
        HouseOrientation.SE to R.string.wizard_orientation_se,
        HouseOrientation.S to R.string.wizard_orientation_s,
        HouseOrientation.SW to R.string.wizard_orientation_sw,
        HouseOrientation.W to R.string.wizard_orientation_w,
        HouseOrientation.NW to R.string.wizard_orientation_nw,
    )
    val topRow = listOf(
        HouseOrientation.NW,
        HouseOrientation.N,
        HouseOrientation.NE,
        HouseOrientation.E,
    )
    val bottomRow = listOf(
        HouseOrientation.W,
        HouseOrientation.S,
        HouseOrientation.SW,
        HouseOrientation.SE,
    )

    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        OrientationRow(
            items = topRow,
            labels = labels,
            selected = selectedOrientation,
            onSelect = onSelect,
        )
        OrientationRow(
            items = bottomRow,
            labels = labels,
            selected = selectedOrientation,
            onSelect = onSelect,
        )
    }
}

@Composable
private fun OrientationRow(
    items: List<HouseOrientation>,
    labels: Map<HouseOrientation, Int>,
    selected: HouseOrientation?,
    onSelect: (HouseOrientation) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        items.forEach { point ->
            SelectableCard(
                modifier = Modifier.weight(1f),
                label = stringResource(labels.getValue(point)),
                selected = selected == point,
                icon = null,
                onClick = { onSelect(point) },
                contentDescription = stringResource(R.string.wizard_orientation_selector),
            )
        }
    }
}