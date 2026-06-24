package com.poultry.broiler.presentation.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poultry.broiler.presentation.theme.PoultryTheme

@Composable
fun NumericInputField(
    value: String,
    unitLabel: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
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
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        readOnly = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun NumericInputFieldPreview() {
    PoultryTheme {
        NumericInputField(
            value = "42.5",
            unitLabel = "kg",
            label = "Poids cible",
        )
    }
}
