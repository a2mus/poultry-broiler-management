package com.poultry.broiler.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.poultry.broiler.R

sealed class NavRoute(
    val route: String,
    val icon: ImageVector,
    val labelResId: Int,
) {
    data object Home : NavRoute(
        route = "home",
        icon = Icons.Filled.Home,
        labelResId = R.string.nav_home,
    )

    data object Wizard : NavRoute(
        route = "wizard",
        icon = Icons.Filled.AutoAwesome,
        labelResId = R.string.nav_wizard,
    )

    data object Dashboard : NavRoute(
        route = "dashboard",
        icon = Icons.Filled.Dashboard,
        labelResId = R.string.nav_dashboard,
    )

    data object Catalog : NavRoute(
        route = "catalog",
        icon = Icons.Filled.Inventory2,
        labelResId = R.string.nav_catalog,
    )

    data object Settings : NavRoute(
        route = "settings",
        icon = Icons.Filled.Settings,
        labelResId = R.string.nav_settings,
    )

    companion object {
        val items = listOf(Home, Wizard, Dashboard, Catalog, Settings)
    }
}
