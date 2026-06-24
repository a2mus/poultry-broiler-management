package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R
import com.poultry.broiler.presentation.theme.BadgeCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Progress badge showing the current wizard step ("Étape {current}/{total}").
 *
 * Visual: rounded badge with `labelSmall` typography, `primary` background and
 * `onPrimary` text, 8dp corners (Constitution Art 3.1 design tokens).
 *
 * @param currentStep 1-based step number.
 * @param totalSteps Total number of wizard steps.
 */
@Composable
fun WizardStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Surface(
        modifier = modifier
            .padding(horizontal = spacing.xs)
            .semantics {
                contentDescription = "Étape $currentStep sur $totalSteps"
            },
        shape = RoundedCornerShape(BadgeCornerRadius),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Text(
            text = stringResource(R.string.wizard_step_indicator, currentStep, totalSteps),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(
                horizontal = spacing.md,
                vertical = spacing.xs,
            ),
        )
    }
}