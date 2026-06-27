package com.poultry.broiler.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Design token spacing system based on an 8dp baseline grid.
 *
 * FR-004: All spacing values as multiples of 4dp:
 * - xxs = 4dp
 * - xs  = 8dp
 * - sm  = 12dp
 * - md  = 16dp
 * - lg  = 24dp
 * - xl  = 32dp
 * - xxl = 48dp
 *
 * Aliases for design system alignment:
 * - micro = 4dp
 * - small = 8dp
 * - medium = 16dp
 * - large = 24dp
 * - baseline = 8dp
 */
data class PoultrySpacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val sm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
    // Design system alignments
    val micro: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val baseline: Dp = 8.dp,
)

val LocalSpacing = staticCompositionLocalOf { PoultrySpacing() }
