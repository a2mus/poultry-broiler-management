package com.poultry.broiler.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.poultry.broiler.presentation.theme.BadgeCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryTheme

@Composable
fun StatusBadge(
    text: String,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier =
            modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(BadgeCornerRadius),
                )
                .padding(vertical = spacing.micro, horizontal = spacing.small),
    )
}

@Preview(showBackground = true)
@Composable
private fun StatusBadgePreview() {
    PoultryTheme {
        StatusBadge(text = "Actif")
    }
}
