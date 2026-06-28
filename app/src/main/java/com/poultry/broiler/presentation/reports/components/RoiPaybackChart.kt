@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.reports.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.presentation.theme.InterFontFamily
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing


@Composable
fun RoiPaybackChart(
    annualProfit: Double,
    capex: Double,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val colorScheme = MaterialTheme.colorScheme
    val textMeasurer = rememberTextMeasurer()
    val yearLabelFormat = stringResource(R.string.financial_roi_year_label)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(spacing.md)) {
            Text(
                text = stringResource(R.string.financial_roi_chart_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(spacing.sm))

            Canvas(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp),
            ) {
                val width = size.width
                val height = size.height
                val padding = 30f

                val usableWidth = width - 2 * padding
                val usableHeight = height - 2 * padding

                // Calculate cumulative cash flow values for Year 0 to 5
                // Year 0 = -capex
                // Year i = -capex + i * annualProfit
                val values = DoubleArray(6) { i -> -capex + i * annualProfit }
                val maxVal = values.maxOrNull()?.coerceAtLeast(0.0) ?: 1.0
                val minVal = values.minOrNull()?.coerceAtMost(0.0) ?: -1.0
                val valRange = maxVal - minVal

                fun toCanvasY(value: Double): Float {
                    val ratio = (value - minVal) / valRange
                    return (padding + usableHeight * (1.0 - ratio)).toFloat()
                }

                fun toCanvasX(index: Int): Float {
                    return padding + (index.toFloat() / 5f) * usableWidth
                }

                // Draw Zero-Line
                val zeroY = toCanvasY(0.0)
                drawLine(
                    color = colorScheme.outline.copy(alpha = 0.5f),
                    start = Offset(padding, zeroY),
                    end = Offset(width - padding, zeroY),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                )

                // Build Path for Cash Flow
                val linePath =
                    Path().apply {
                        moveTo(toCanvasX(0), toCanvasY(values[0]))
                        for (i in 1..5) {
                            lineTo(toCanvasX(i), toCanvasY(values[i]))
                        }
                    }

                // Build Area Path for Gradient fill
                val areaPath =
                    Path().apply {
                        moveTo(toCanvasX(0), zeroY)
                        for (i in 0..5) {
                            lineTo(toCanvasX(i), toCanvasY(values[i]))
                        }
                        lineTo(toCanvasX(5), zeroY)
                        close()
                    }

                // Draw Area Gradient
                drawPath(
                    path = areaPath,
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    colorScheme.primary.copy(alpha = 0.3f),
                                    colorScheme.primary.copy(alpha = 0.0f),
                                ),
                            startY = padding,
                            endY = height - padding,
                        ),
                )

                // Draw Line
                drawPath(
                    path = linePath,
                    color = colorScheme.primary,
                    style = Stroke(width = 4f),
                )

                // Draw Dots & Year Labels
                val labelStyle = TextStyle(fontFamily = InterFontFamily, fontSize = 10.sp, color = colorScheme.onSurfaceVariant)
                for (i in 0..5) {
                    val cx = toCanvasX(i)
                    val cy = toCanvasY(values[i])

                    // Draw Data Dot
                    drawCircle(
                        color = colorScheme.primary,
                        radius = 6f,
                        center = Offset(cx, cy),
                    )
                    drawCircle(
                        color = colorScheme.surface,
                        radius = 3f,
                        center = Offset(cx, cy),
                    )

                    // Draw Year Label below
                    val yearLabel = yearLabelFormat.format(i)
                    val textLayout = textMeasurer.measure(yearLabel, labelStyle)
                    drawText(
                        textLayoutResult = textLayout,
                        topLeft = Offset(cx - textLayout.size.width / 2, height - padding + 4f),
                    )
                }
            }
        }
    }
}
