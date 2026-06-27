@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.design.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Custom vector graphics drawing functions for the 2D blueprint layout symbols.
 */
object BlueprintSymbols {
    fun DrawScope.drawExhaustFan(
        center: Offset,
        radius: Float,
        color: Color,
    ) {
        // Outer ring
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = 2f),
        )
        // Hub
        drawCircle(
            color = color,
            radius = radius * 0.25f,
            center = center,
        )
        // 4 Propeller blades
        val bladePath =
            Path().apply {
                // Blade 1 (Top)
                moveTo(center.x, center.y)
                quadraticBezierTo(
                    center.x - radius * 0.3f,
                    center.y - radius * 0.5f,
                    center.x,
                    center.y - radius * 0.9f,
                )
                quadraticBezierTo(
                    center.x + radius * 0.3f,
                    center.y - radius * 0.5f,
                    center.x,
                    center.y,
                )

                // Blade 2 (Right)
                moveTo(center.x, center.y)
                quadraticBezierTo(
                    center.x + radius * 0.5f,
                    center.y - radius * 0.3f,
                    center.x + radius * 0.9f,
                    center.y,
                )
                quadraticBezierTo(
                    center.x + radius * 0.5f,
                    center.y + radius * 0.3f,
                    center.x,
                    center.y,
                )

                // Blade 3 (Bottom)
                moveTo(center.x, center.y)
                quadraticBezierTo(
                    center.x + radius * 0.3f,
                    center.y + radius * 0.5f,
                    center.x,
                    center.y + radius * 0.9f,
                )
                quadraticBezierTo(
                    center.x - radius * 0.3f,
                    center.y + radius * 0.5f,
                    center.x,
                    center.y,
                )

                // Blade 4 (Left)
                moveTo(center.x, center.y)
                quadraticBezierTo(
                    center.x - radius * 0.5f,
                    center.y + radius * 0.3f,
                    center.x - radius * 0.9f,
                    center.y,
                )
                quadraticBezierTo(
                    center.x - radius * 0.5f,
                    center.y - radius * 0.3f,
                    center.x,
                    center.y,
                )
            }
        drawPath(path = bladePath, color = color)
    }

    fun DrawScope.drawHeater(
        center: Offset,
        size: Float,
        color: Color,
    ) {
        val flamePath =
            Path().apply {
                val startX = center.x
                val startY = center.y + size * 0.5f
                moveTo(startX, startY)
                // Left curve to tip
                cubicTo(
                    startX - size * 0.6f,
                    startY - size * 0.1f,
                    startX - size * 0.3f,
                    startY - size * 0.8f,
                    startX,
                    startY - size * 0.9f,
                )
                // Right curve back to start
                cubicTo(
                    startX + size * 0.3f,
                    startY - size * 0.8f,
                    startX + size * 0.6f,
                    startY - size * 0.1f,
                    startX,
                    startY,
                )
                // Draw an inner smaller flame
                moveTo(startX, startY - size * 0.1f)
                cubicTo(
                    startX - size * 0.3f,
                    startY - size * 0.2f,
                    startX - size * 0.15f,
                    startY - size * 0.6f,
                    startX,
                    startY - size * 0.7f,
                )
                cubicTo(
                    startX + size * 0.15f,
                    startY - size * 0.6f,
                    startX + size * 0.3f,
                    startY - size * 0.2f,
                    startX,
                    startY - size * 0.1f,
                )
            }
        drawPath(path = flamePath, color = color)
    }

    fun DrawScope.drawCoolingPad(
        topLeft: Offset,
        size: Size,
        color: Color,
    ) {
        // Outline box
        drawRect(
            color = color,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 2f),
        )
        // Draw diagonal/zig-zag hatched lines inside
        val numLines = 6
        val step = size.height / (numLines + 1)
        for (i in 1..numLines) {
            val y = topLeft.y + i * step
            drawLine(
                color = color,
                start = Offset(topLeft.x, y),
                end = Offset(topLeft.x + size.width, y + step * 0.5f),
                strokeWidth = 2f,
            )
        }
    }

    fun DrawScope.drawFeedLine(
        start: Offset,
        end: Offset,
        color: Color,
    ) {
        // Main line
        drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
        )
        // Feed pans (spaced circles along the line)
        val dx = end.x - start.x
        val dy = end.y - start.y
        val length = kotlin.math.hypot(dx, dy)
        val steps = (length / 40f).toInt().coerceAtLeast(3)
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            val panCenter = Offset(start.x + dx * t, start.y + dy * t)
            drawCircle(
                color = color,
                radius = 8f,
                center = panCenter,
            )
        }
    }

    fun DrawScope.drawWaterLine(
        start: Offset,
        end: Offset,
        color: Color,
    ) {
        // Main line
        drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = 2f,
            cap = StrokeCap.Round,
        )
        // Nipple dots (smaller blue circles along the line)
        val dx = end.x - start.x
        val dy = end.y - start.y
        val length = kotlin.math.hypot(dx, dy)
        val steps = (length / 25f).toInt().coerceAtLeast(4)
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            val nippleCenter = Offset(start.x + dx * t, start.y + dy * t)
            drawCircle(
                color = color,
                radius = 4f,
                center = nippleCenter,
            )
        }
    }
}
