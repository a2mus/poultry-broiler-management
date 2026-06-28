package com.poultry.broiler.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Design token elevation system with 4 levels.
 *
 * FR-006:
 * - flat  = 0dp
 * - card  = 2dp
 * - hover = 6dp
 * - modal = 12dp
 */
object PoultryElevation {
    val flat: Dp = 0.dp
    val card: Dp = 2.dp
    val hover: Dp = 6.dp
    val modal: Dp = 12.dp
}
