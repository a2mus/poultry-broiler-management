package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.unit.dp
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryElevation

/**
 * Shared selectable card used by the wizard's segmented selectors.
 *
 * The card shows an optional [icon] and a [label]; when [selected] it adopts the
 * `primaryContainer` background and displays a checkmark overlay. Every card
 * provides a 48dp touch target (Constitution Art 3.3) and a localized
 * `contentDescription` for screen readers.
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
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .semantics { this.contentDescription = contentDescription ?: label },
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) colorScheme.primaryContainer else colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) PoultryElevation.card else PoultryElevation.flat,
        ),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Box(
                modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selected) colorScheme.onPrimaryContainer else colorScheme.onSurfaceVariant,
                    )
                }
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.padding(start = 32.dp),
                    )
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) colorScheme.onPrimaryContainer else colorScheme.onSurface,
            )
        }
    }
}