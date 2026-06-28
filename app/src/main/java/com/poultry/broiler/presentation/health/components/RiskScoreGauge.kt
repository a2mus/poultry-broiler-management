@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.health.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poultry.broiler.R
import com.poultry.broiler.presentation.theme.InterFontFamily


/**
 * Radial compliance risk progress gauge (FR-016).
 *
 * Displays a color-coded radial arc representing compliance safety level.
 *
 * @param score Current compliance safety score (0 to 100).
 */
@Composable
fun RiskScoreGauge(
    score: Int,
    modifier: Modifier = Modifier,
) {
    val trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)

    // Determine color based on standard risk thresholds
    val progressColor =
        when {
            score >= 80 -> Color(0xFF2ECC71) // Safe / Vibrant Mint
            score >= 50 -> Color(0xFFF1C40F) // Warning / Yellow
            else -> Color(0xFFE74C3C) // High Risk / Red
        }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val strokeWidth = 16.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            // Draw track (240-degree background arc from 150 to 390 degrees)
            drawArc(
                color = trackColor,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )

            // Draw active progress arc
            val sweepAngle = (score.toFloat() / 100f) * 240f
            drawArc(
                color = progressColor,
                startAngle = 150f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }

        // Text inside the gauge
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score%",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = InterFontFamily,
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text =
                    if (score >= 80) {
                        stringResource(R.string.risk_status_conforming)
                    } else if (score >= 50) {
                        stringResource(R.string.risk_status_warning)
                    } else {
                        stringResource(R.string.risk_status_danger)
                    },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = progressColor,
                letterSpacing = 1.sp,
            )
        }
    }
}
