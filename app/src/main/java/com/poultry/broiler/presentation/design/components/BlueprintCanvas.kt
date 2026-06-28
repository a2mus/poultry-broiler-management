@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.poultry.broiler.presentation.design.components.BlueprintSymbols.drawCoolingPad
import com.poultry.broiler.presentation.design.components.BlueprintSymbols.drawExhaustFan
import com.poultry.broiler.presentation.design.components.BlueprintSymbols.drawFeedLine
import com.poultry.broiler.presentation.design.components.BlueprintSymbols.drawHeater
import com.poultry.broiler.presentation.design.components.BlueprintSymbols.drawWaterLine

/**
 * Zoomable, pannable 2D blueprint layout canvas (FR-013, FR-014).
 *
 * Displays a custom grid background and positions fans, heaters, cooling pads,
 * feed, and water lines inside the house outline.
 */
@Composable
fun BlueprintCanvas(
    length: Double,
    width: Double,
    scale: Float,
    offset: Offset,
    onTransform: (Float, Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    Canvas(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(0.5f, 4.0f)
                        onTransform(newScale, offset + pan)
                    }
                }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw grid lines in the background
        val gridSpacing = 40f
        val gridColor = colorScheme.outlineVariant.copy(alpha = 0.3f)

        // Draw vertical grid lines
        var x = 0f
        while (x < canvasWidth) {
            drawLine(gridColor, Offset(x, 0f), Offset(x, canvasHeight), 1f)
            x += gridSpacing
        }
        // Draw horizontal grid lines
        var y = 0f
        while (y < canvasHeight) {
            drawLine(gridColor, Offset(0f, y), Offset(canvasWidth, y), 1f)
            y += gridSpacing
        }

        // Define house rectangle bounds (centered in canvas)
        val aspect = (length / width).toFloat()
        var rectWidth = canvasWidth * 0.75f
        var rectHeight = rectWidth / aspect
        if (rectHeight > canvasHeight * 0.75f) {
            rectHeight = canvasHeight * 0.75f
            rectWidth = rectHeight * aspect
        }

        val left = (canvasWidth - rectWidth) / 2f
        val top = (canvasHeight - rectHeight) / 2f
        val rect = Rect(left, top, left + rectWidth, top + rectHeight)

        // Draw outer building walls
        drawRect(
            color = colorScheme.primary,
            topLeft = Offset(rect.left, rect.top),
            size = Size(rect.width, rect.height),
            style = Stroke(width = 4f),
        )

        // Draw Cooling Pads (top and bottom walls, left end)
        val padWidth = rectWidth * 0.15f
        val padHeight = 8f
        // Top Cooling Pad
        drawCoolingPad(
            topLeft = Offset(rect.left + rectWidth * 0.05f, rect.top - padHeight / 2),
            size = Size(padWidth, padHeight),
            color = colorScheme.secondary,
        )
        // Bottom Cooling Pad
        drawCoolingPad(
            topLeft = Offset(rect.left + rectWidth * 0.05f, rect.bottom - padHeight / 2),
            size = Size(padWidth, padHeight),
            color = colorScheme.secondary,
        )

        // Draw Exhaust Fans (right end wall)
        val fanRadius = 12f
        val numFans = 4
        val fanSpacing = rectHeight / (numFans + 1)
        for (i in 1..numFans) {
            val fanY = rect.top + i * fanSpacing
            drawExhaustFan(
                center = Offset(rect.right, fanY),
                radius = fanRadius,
                color = colorScheme.onSurface,
            )
        }

        // Draw Feed Lines (longitudinal, middle section)
        val feedY1 = rect.top + rectHeight * 0.3f
        val feedY2 = rect.top + rectHeight * 0.7f
        // Reddish-brown for feed
        drawFeedLine(
            start = Offset(rect.left + rectWidth * 0.1f, feedY1),
            end = Offset(rect.right - rectWidth * 0.1f, feedY1),
            color = Color(0xFFC0392B),
        )
        drawFeedLine(
            start = Offset(rect.left + rectWidth * 0.1f, feedY2),
            end = Offset(rect.right - rectWidth * 0.1f, feedY2),
            color = Color(0xFFC0392B),
        )

        // Draw Water Lines (longitudinal, between feed lines)
        val waterY1 = rect.top + rectHeight * 0.15f
        val waterY2 = rect.top + rectHeight * 0.5f
        val waterY3 = rect.top + rectHeight * 0.85f
        // Water blue
        drawWaterLine(
            start = Offset(rect.left + rectWidth * 0.08f, waterY1),
            end = Offset(rect.right - rectWidth * 0.08f, waterY1),
            color = Color(0xFF2980B9),
        )
        drawWaterLine(
            start = Offset(rect.left + rectWidth * 0.08f, waterY2),
            end = Offset(rect.right - rectWidth * 0.08f, waterY2),
            color = Color(0xFF2980B9),
        )
        drawWaterLine(
            start = Offset(rect.left + rectWidth * 0.08f, waterY3),
            end = Offset(rect.right - rectWidth * 0.08f, waterY3),
            color = Color(0xFF2980B9),
        )

        // Draw Heaters (centered, between feed/water lines)
        val heaterSize = 20f
        val numHeaters = 3
        val heaterSpacing = rectWidth / (numHeaters + 1)
        for (i in 1..numHeaters) {
            val heaterX = rect.left + i * heaterSpacing
            // Orange flame
            drawHeater(
                center = Offset(heaterX, rect.top + rectHeight * 0.5f),
                size = heaterSize,
                color = Color(0xFFD35400),
            )
        }
    }
}
