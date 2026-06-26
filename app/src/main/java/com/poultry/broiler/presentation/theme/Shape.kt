package com.poultry.broiler.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Design token shapes for Poultry Broiler Management.
 *
 * FR-005: All corner radii specified via design tokens.
 */
val BadgeCornerRadius = 8.dp
val CardCornerRadius = 16.dp
val ButtonCornerRadius = 24.dp
val DialogCornerRadius = 28.dp

val PoultryShapes =
    Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(BadgeCornerRadius),
        medium = RoundedCornerShape(CardCornerRadius),
        large = RoundedCornerShape(ButtonCornerRadius),
        extraLarge = RoundedCornerShape(DialogCornerRadius),
    )
