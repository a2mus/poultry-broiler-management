package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/**
 * Bottom navigation bar with Previous / Next buttons supporting RTL mirroring.
 *
 * The Previous button is `OutlinedButton` and the Next button is filled. Both
 * have 24dp pill corners and a 48dp minimum touch target (Art 3.3). In RTL
 * layouts the buttons swap positions visually while preserving their roles.
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
        OutlinedButton(
            onClick = onPrevious,
            enabled = canGoPrevious,
            shape = RoundedCornerShape(ButtonCornerRadius),
            modifier =
                m
                    .defaultMinSize(minHeight = 48.dp)
                    .semantics {
                        contentDescription = "Revenir à l'étape précédente Navigation Button"
                    },
        ) {
            Box(modifier = Modifier.defaultMinSize(minHeight = 48.dp)) {
                Text(text = stringResource(R.string.wizard_button_previous))
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
                    .defaultMinSize(minHeight = 48.dp)
                    .semantics {
                        contentDescription = "Passer à l'étape suivante Navigation Button"
                    },
        ) {
            Box(modifier = Modifier.defaultMinSize(minHeight = 48.dp)) {
                Text(text = stringResource(R.string.wizard_button_next))
            }
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
