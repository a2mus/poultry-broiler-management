package com.poultry.broiler.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme =
    lightColorScheme(
        primary = ForestTealLightPrimary,
        onPrimary = ForestTealLightOnPrimary,
        primaryContainer = ForestTealLightPrimaryContainer,
        onPrimaryContainer = ForestTealLightOnPrimaryContainer,
        secondary = ForestTealLightSecondary,
        onSecondary = ForestTealLightOnSecondary,
        secondaryContainer = ForestTealLightSecondaryContainer,
        onSecondaryContainer = ForestTealLightOnSecondaryContainer,
        tertiary = ForestTealLightTertiary,
        onTertiary = ForestTealLightOnTertiary,
        tertiaryContainer = ForestTealLightTertiaryContainer,
        onTertiaryContainer = ForestTealLightOnTertiaryContainer,
        error = ForestTealLightError,
        onError = ForestTealLightOnError,
        errorContainer = ForestTealLightErrorContainer,
        onErrorContainer = ForestTealLightOnErrorContainer,
        background = ForestTealLightBackground,
        onBackground = ForestTealLightOnBackground,
        surface = ForestTealLightSurface,
        onSurface = ForestTealLightOnSurface,
        surfaceVariant = ForestTealLightSurfaceVariant,
        onSurfaceVariant = ForestTealLightOnSurfaceVariant,
        outline = ForestTealLightOutline,
        outlineVariant = ForestTealLightOutlineVariant,
        inverseSurface = ForestTealLightInverseSurface,
        inverseOnSurface = ForestTealLightInverseOnSurface,
        inversePrimary = ForestTealLightInversePrimary,
        surfaceTint = ForestTealLightSurfaceTint,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = SleekCarbonDarkPrimary,
        onPrimary = SleekCarbonDarkOnPrimary,
        primaryContainer = SleekCarbonDarkPrimaryContainer,
        onPrimaryContainer = SleekCarbonDarkOnPrimaryContainer,
        secondary = SleekCarbonDarkSecondary,
        onSecondary = SleekCarbonDarkOnSecondary,
        secondaryContainer = SleekCarbonDarkSecondaryContainer,
        onSecondaryContainer = SleekCarbonDarkOnSecondaryContainer,
        tertiary = SleekCarbonDarkTertiary,
        onTertiary = SleekCarbonDarkOnTertiary,
        tertiaryContainer = SleekCarbonDarkTertiaryContainer,
        onTertiaryContainer = SleekCarbonDarkOnTertiaryContainer,
        error = SleekCarbonDarkError,
        onError = SleekCarbonDarkOnError,
        errorContainer = SleekCarbonDarkErrorContainer,
        onErrorContainer = SleekCarbonDarkOnErrorContainer,
        background = SleekCarbonDarkBackground,
        onBackground = SleekCarbonDarkOnBackground,
        surface = SleekCarbonDarkSurface,
        onSurface = SleekCarbonDarkOnSurface,
        surfaceVariant = SleekCarbonDarkSurfaceVariant,
        onSurfaceVariant = SleekCarbonDarkOnSurfaceVariant,
        outline = SleekCarbonDarkOutline,
        outlineVariant = SleekCarbonDarkOutlineVariant,
        inverseSurface = SleekCarbonDarkInverseSurface,
        inverseOnSurface = SleekCarbonDarkInverseOnSurface,
        inversePrimary = SleekCarbonDarkInversePrimary,
        surfaceTint = SleekCarbonDarkSurfaceTint,
    )

@Composable
fun PoultryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalSpacing provides PoultrySpacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PoultryTypography,
            shapes = PoultryShapes,
            content = content,
        )
    }
}
