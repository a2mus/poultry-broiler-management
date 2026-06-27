package com.poultry.broiler.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.LineWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryTheme

/**
 * Enhanced numeric text field with iconography, better focus states, and improved visual hierarchy.
 *
 * Improvements over basic Material Design:
 * - Iconography for visual affordance
 * - Custom focus colors with primary tint
 * - Enhanced container colors for focus state
 * - Min height 56dp (improved from 48dp for better touch targets)
 *
 * @param value Current text value.
 * @param onValueChange Callback invoked on each keystroke.
 * @param label Field label.
 * @param unitLabel Trailing unit label (e.g., "m", "mm").
 * @param errorMessage Inline error message; `null` clears the error state.
 * @param enabled Whether the field accepts input.
 * @param modifier Optional layout modifier.
 */
@Composable
fun NumericInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unitLabel: String,
    errorMessage: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    // Select appropriate icon based on unit label
    val leadingIcon = @Composable {
        Icon(
            imageVector = when {
                label.contains("Longueur", ignoreCase = true) ||
                label.contains("Length", ignoreCase = true) -> Icons.Outlined.Straighten
                label.contains("Largeur", ignoreCase = true) ||
                label.contains("Width", ignoreCase = true) -> Icons.Outlined.LineWeight
                label.contains("Hauteur", ignoreCase = true) ||
                label.contains("Height", ignoreCase = true) ||
                label.contains("Crêt", ignoreCase = true) ||
                label.contains("Ridge", ignoreCase = true) -> Icons.Outlined.Height
                else -> Icons.Outlined.Straighten
            },
            contentDescription = null,
            tint = if (errorMessage != null) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            leadingIcon = leadingIcon,
            trailingIcon = {
                Text(
                    text = unitLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (errorMessage != null) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = MaterialTheme.typography.bodyLarge,
            isError = errorMessage != null,
            enabled = enabled,
            singleLine = true,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = 0.05f
                ),
            ),
            shape = MaterialTheme.shapes.large,
        )
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier =
                    Modifier.padding(
                        start = spacing.md,
                        top = spacing.xs,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NumericInputFieldPreview() {
    PoultryTheme {
        NumericInputField(
            value = "42.5",
            onValueChange = {},
            unitLabel = stringResource(R.string.unit_kilograms),
            label = "Poids cible",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NumericInputFieldErrorPreview() {
    PoultryTheme {
        NumericInputField(
            value = "0",
            onValueChange = {},
            unitLabel = stringResource(R.string.unit_meters),
            label = "Longueur",
            errorMessage = "La valeur doit être supérieure à 0",
        )
    }
}
