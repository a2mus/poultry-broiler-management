@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.presentation.design.components.BlueprintCanvas
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlueprintScreen(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current
    var zoomScale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    val houseLength = 120.0
    val houseWidth = 15.0

    Box(modifier = modifier.fillMaxSize()) {
        // Blueprint interactive canvas
        BlueprintCanvas(
            length = houseLength,
            width = houseWidth,
            scale = zoomScale,
            offset = panOffset,
            onTransform = { scale, offset ->
                zoomScale = scale
                panOffset = offset
            },
            modifier = Modifier.fillMaxSize(),
        )

        // Floating Zoom Controls (Top Right)
        Column(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column {
                    IconButton(
                        onClick = { zoomScale = (zoomScale + 0.25f).coerceAtMost(4.0f) },
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Zoom In",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    IconButton(
                        onClick = { zoomScale = (zoomScale - 0.25f).coerceAtLeast(0.5f) },
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Zoom Out",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.padding(top = spacing.xxs),
            ) {
                Text(
                    text = "${(zoomScale * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = spacing.sm, vertical = spacing.xxs),
                )
            }
        }

        // Legend Footer (Bottom Panel)
        Card(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(spacing.md),
            shape = RoundedCornerShape(CardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(modifier = Modifier.padding(spacing.md)) {
                Text(
                    text = stringResource(R.string.blueprint_legend_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(spacing.sm))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing.md),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LegendItem(color = MaterialTheme.colorScheme.onSurface, label = stringResource(R.string.blueprint_legend_fans))
                    LegendItem(color = Color(0xFFD35400), label = stringResource(R.string.blueprint_legend_heaters))
                    LegendItem(color = MaterialTheme.colorScheme.secondary, label = stringResource(R.string.blueprint_legend_cooling_pads))
                    LegendItem(color = Color(0xFFC0392B), label = stringResource(R.string.blueprint_legend_feeders))
                    LegendItem(color = Color(0xFF2980B9), label = stringResource(R.string.blueprint_legend_drinkers))
                }
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
) {
    val spacing = LocalSpacing.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                Modifier
                    .size(16.dp, 8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color),
        )
        Spacer(modifier = Modifier.width(spacing.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
