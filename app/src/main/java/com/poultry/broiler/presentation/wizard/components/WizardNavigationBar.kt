package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R
import com.poultry.broiler.presentation.theme.ButtonCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryElevation

/**
 * Enhanced bottom navigation bar with iconography, better visual weight, and improved accessibility.
 *
 * Improvements over basic Material Design:
 * - Iconography for Previous/Next actions
 * - Enhanced button elevation (PoultryElevation.card for visual depth)
 * - Increased touch target size (56dp minimum)
 * - Better visual hierarchy with primary colors
 * - Improved disabled states
 *
 * @param canGoNext Enables / disables the Next button.
 * @param canGoPrevious Enables / disables the Previous button (false on Step 1).
 * @param onNext Invoked when the user taps Next.
 * @param onPrevious Invoked when the user taps Previous.
 */
@Composable
fun WizardNavigationBar(
    canGoNext: Boolean,
    canGoPrevious: Boolean,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val previousButton: @Composable (Modifier) -> Unit = { m ->
        Surface(
            onClick = onPrevious,
            enabled = canGoPrevious,
            shape = RoundedCornerShape(ButtonCornerRadius),
            modifier =
                m
                    .defaultMinSize(minHeight = 56.dp)
                    .semantics {
                        contentDescription = "Revenir à l'étape précédente"
                    },
            tonalElevation = PoultryElevation.card,
            border =
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (canGoPrevious) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    },
                ),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.height(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.wizard_button_previous),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }

    val nextButton: @Composable (Modifier) -> Unit = { m ->
        Button(
            onClick = onNext,
            enabled = canGoNext,
            shape = RoundedCornerShape(ButtonCornerRadius),
            modifier =
                m
                    .defaultMinSize(minHeight = 56.dp)
                    .semantics {
                        contentDescription = "Passer à l'étape suivante"
                    },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor =
                        MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.5f,
                        ),
                ),
            elevation =
                ButtonDefaults.buttonElevation(
                    defaultElevation = PoultryElevation.card,
                    pressedElevation = PoultryElevation.hover,
                ),
        ) {
            Text(
                text = stringResource(R.string.wizard_button_next),
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.height(20.dp),
            )
        }
    }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        if (isRtl) {
            nextButton(Modifier.weight(1f))
            previousButton(Modifier.weight(1f))
        } else {
            previousButton(Modifier.weight(1f))
            nextButton(Modifier.weight(1f))
        }
    }
}
