package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryElevation

/**
 * Shared selectable card used by the wizard's segmented selectors.
 *
 * Designed with a Column layout to optimize width for multilingual labels
 * (French / Arabic) and prevent text wrapping/truncation in compact rows.
 *
 * @param label Visible text label below the icon.
 * @param selected Whether this option is currently active.
 * @param icon Optional decorative silhouette icon.
 * @param onClick Invoked when the card is tapped.
 * @param contentDescription Screen-reader label (falls back to [label]).
 */
@Composable
internal fun SelectableCard(
    label: String,
    selected: Boolean,
    icon: ImageVector?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val spacing = LocalSpacing.current
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier =
            modifier
                .defaultMinSize(minHeight = 84.dp)
                .fillMaxWidth()
                .semantics { this.contentDescription = contentDescription ?: label },
        shape = RoundedCornerShape(CardCornerRadius),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected) {
                        colorScheme.primaryContainer.copy(alpha = 0.15f)
                    } else {
                        colorScheme.surface
                    },
            ),
        border =
            BorderStroke(
                width = if (selected) 2.dp else 1.dp,
                color =
                    if (selected) {
                        colorScheme.primary
                    } else {
                        colorScheme.outlineVariant
                    },
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = if (selected) PoultryElevation.card else PoultryElevation.flat,
            ),
        onClick = onClick,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.xs, vertical = spacing.sm),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selected) colorScheme.primary else colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(modifier = Modifier.height(spacing.xs))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) colorScheme.primary else colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier =
                        Modifier
                            .size(16.dp)
                            .align(Alignment.TopEnd),
                )
            }
        }
    }
}
