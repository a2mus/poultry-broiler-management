package com.poultry.broiler.presentation.wizard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.presentation.theme.LocalSpacing
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val TABLET_MIN_WIDTH_DP = 600

/**
 * Live 2D top-down preview of the building envelope with ruler markers and an
 * optional compass indicator (research.md §3).
 *
 * Visual:
 * - proportionally scaled rectangle (stroke, no fill) representing the envelope
 * - ruler markers along all four edges with "{value}m" labels
 * - compass rose indicator in the top-right corner reflecting [orientation]
 * - background uses `surfaceVariant`, outline `primary`, rulers `onSurface`
 * - minimum 40dp rectangle so extreme small dimensions remain visible (FR-021)
 * - fixed height 200dp (phone) / 280dp (tablet)
 *
 * Canvas uses prime math coordinates; RTL mirroring does NOT flip the geometry
 * (Constitution Art 8.2 — only navigation chrome is mirrored, never data).
 *
 * @param length Building length in meters; `null` while not yet entered.
 * @param width Building width in meters; `null` while not yet entered.
 * @param orientation Compass heading; `null` hides the compass indicator.
 */
@Composable
fun DimensionPreviewCanvas(
    length: Double?,
    width: Double?,
    orientation: HouseOrientation?,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= TABLET_MIN_WIDTH_DP
    val canvasHeight = if (isTablet) 280.dp else 200.dp
    val spacing = LocalSpacing.current

    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    val colorScheme = MaterialTheme.colorScheme

    val previewDescription = stringResource(R.string.cd_wizard_preview_canvas)

    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(canvasHeight)
                .semantics { contentDescription = previewDescription },
    ) {
        val density = this
        val canvasWidth = size.width
        val canvasHeightPx = size.height

        fun dpPx(dp: androidx.compose.ui.unit.Dp): Float = with(density) { dp.toPx() }

        val paddingPx = dpPx(spacing.lg)
        val minDimPx = dpPx(40.dp)
        val labelStyle = TextStyle(fontSize = 11.sp, color = colorScheme.onSurface)

        // Background
        drawRect(color = colorScheme.surfaceVariant, topLeft = Offset.Zero, size = size)

        val usableWidth = canvasWidth - 2 * paddingPx
        val usableHeight = canvasHeightPx - 2 * paddingPx

        val rect: Rect =
            if (length != null && width != null && length > 0.0 && width > 0.0) {
                val aspect = (length / width).toFloat()
                var rectWidth = usableWidth
                var rectHeight = usableWidth / aspect
                if (rectHeight > usableHeight) {
                    rectHeight = usableHeight
                    rectWidth = usableHeight * aspect
                }
                if (rectWidth < minDimPx) rectWidth = minDimPx
                if (rectHeight < minDimPx) rectHeight = minDimPx
                val left = (canvasWidth - rectWidth) / 2f
                val top = (canvasHeightPx - rectHeight) / 2f
                Rect(left, top, left + rectWidth, top + rectHeight)
            } else {
                val side = min(usableWidth, usableHeight)
                val left = (canvasWidth - side) / 2f
                val top = (canvasHeightPx - side) / 2f
                Rect(left, top, left + side, top + side)
            }

        // Outline
        drawRect(
            color = colorScheme.primary,
            topLeft = Offset(rect.left, rect.top),
            size = Size(rect.width, rect.height),
            style = Stroke(width = dpPx(2.dp)),
        )

        // Dimension labels + ruler ticks
        if (length != null && width != null && length > 0.0 && width > 0.0) {
            val lengthLabel = "%.1fm".format(length)
            val widthLabel = "%.1fm".format(width)
            val lengthText = textMeasurer.measure(lengthLabel, labelStyle)
            val widthText = textMeasurer.measure(widthLabel, labelStyle)

            drawText(
                textLayoutResult = lengthText,
                topLeft =
                    Offset(
                        x = rect.center.x - lengthText.size.width / 2f,
                        y = rect.top - dpPx(16.dp),
                    ),
            )
            drawText(
                textLayoutResult = widthText,
                topLeft =
                    Offset(
                        x = rect.left - dpPx(32.dp),
                        y = rect.center.y - widthText.size.height / 2f,
                    ),
            )

            val tick = dpPx(8.dp)
            val stroke = dpPx(1.dp)
            drawLine(
                colorScheme.onSurface,
                Offset(rect.left, rect.top - tick),
                Offset(rect.right, rect.top - tick),
                stroke,
            )
            drawLine(
                colorScheme.onSurface,
                Offset(rect.left - tick, rect.top),
                Offset(rect.left - tick, rect.bottom),
                stroke,
            )
            drawLine(
                colorScheme.onSurface,
                Offset(rect.left, rect.bottom + tick),
                Offset(rect.right, rect.bottom + tick),
                stroke,
            )
            drawLine(
                colorScheme.onSurface,
                Offset(rect.right + tick, rect.top),
                Offset(rect.right + tick, rect.bottom),
                stroke,
            )
        }

        // Compass indicator
        if (orientation != null) {
            val compassRadius = dpPx(20.dp)
            val compassCenter =
                Offset(
                    x = canvasWidth - paddingPx - compassRadius,
                    y = paddingPx + compassRadius,
                )
            drawCircle(
                color = colorScheme.onSurface,
                radius = compassRadius,
                center = compassCenter,
                style = Stroke(width = dpPx(2.dp)),
            )
            val radians = Math.toRadians(orientation.degrees.toDouble()) - Math.PI / 2
            val needle =
                Offset(
                    x = compassCenter.x + (compassRadius * cos(radians)).toFloat() * 0.8f,
                    y = compassCenter.y + (compassRadius * sin(radians)).toFloat() * 0.8f,
                )
            drawLine(
                color = colorScheme.primary,
                start = compassCenter,
                end = needle,
                strokeWidth = dpPx(2.dp),
                cap = StrokeCap.Round,
            )
            val label = orientation.name
            val labelText = textMeasurer.measure(label, labelStyle.copy(color = colorScheme.onSurfaceVariant))
            drawText(
                textLayoutResult = labelText,
                topLeft =
                    Offset(
                        x = compassCenter.x - labelText.size.width / 2f,
                        y = compassCenter.y + compassRadius + dpPx(2.dp),
                    ),
            )
        }
    }
}
