@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.health.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing

@Composable
fun SensorGrid(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            SensorCard(
                title = "Température",
                value = "24.8 °C",
                statusText = "NORMAL",
                statusColor = Color(0xFF2ECC71),
                modifier = Modifier.weight(1f),
            )
            SensorCard(
                title = "Humidité",
                value = "62.4 %",
                statusText = "NORMAL",
                statusColor = Color(0xFF2ECC71),
                modifier = Modifier.weight(1f),
            )
        }
        SensorCard(
            title = "Qualité de l'Air (CO₂)",
            value = "1,150 ppm",
            statusText = "ATTENTION",
            statusColor = Color(0xFFF1C40F),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SensorCard(
    title: String,
    value: String,
    statusText: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    // Pulsing indicator setup
    val infiniteTransition = rememberInfiniteTransition(label = "indicatorTransition")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "pulseAlpha",
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(spacing.md)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier =
                            Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .graphicsLayer(alpha = alpha)
                                .background(statusColor),
                    )
                    Spacer(modifier = Modifier.width(spacing.xxs))
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                    )
                }
            }
            Spacer(modifier = Modifier.height(spacing.xs))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
