package com.poultry.broiler.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R
import com.poultry.broiler.presentation.theme.PoultryTheme
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Numeric text field with a trailing unit label and optional inline error.
 *
 * Previously read-only (Feature #001 catalogue), this composable now accepts
 * user input via [onValueChange] and surfaces inline errors via [errorMessage]
 * (House Dimensions Wizard — Constitution Art 4.3 dual-layer validation).
 *
 * Visual:
 * - `OutlinedTextField` with trailing [unitLabel] text.
 * - `KeyboardType.Decimal` keypad.
 * - Min height 48dp (Constitution Art 3.3 touch-target baseline).
 * - Error state: bordered with [MaterialTheme.colorScheme.error] and the
 *   [errorMessage] rendered below in `labelSmall` error color.
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
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            trailingIcon = {
                Text(
                    text = unitLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = MaterialTheme.typography.bodyLarge,
            isError = errorMessage != null,
            enabled = enabled,
            singleLine = true,
            modifier = Modifier.defaultMinSize(minHeight = 48.dp),
        )
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(
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