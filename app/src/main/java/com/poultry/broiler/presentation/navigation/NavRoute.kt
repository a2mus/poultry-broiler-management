package com.poultry.broiler.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import com.poultry.broiler.R

/**
 * Navigation routes for the application.
 *
 * [bottomNavItems] defines the four primary tabs (FR-017).
 * Wizard and Dashboard are non-tab destinations reached from the Home Screen.
 */
sealed class NavRoute(
    val route: String,
    val icon: ImageVector,
    val labelResId: Int,
    val contentDescriptionResId: Int,
) {
    // ── Bottom Navigation Tabs ──────────────────────────────────────

    /** Projects tab — the Home Screen with the project list. */
    data object Projects : NavRoute(
        route = ROUTE_PROJECTS,
        icon = Icons.Filled.ViewList,
        labelResId = R.string.nav_projects,
        contentDescriptionResId = R.string.cd_projects_nav,
    )

    /** Design tab — stub for future features. */
    data object Design : NavRoute(
        route = ROUTE_DESIGN,
        icon = Icons.Filled.DesignServices,
        labelResId = R.string.nav_design,
        contentDescriptionResId = R.string.cd_design_nav,
    )

    /** Reports tab — stub for future features. */
    data object Reports : NavRoute(
        route = ROUTE_REPORTS,
        icon = Icons.Filled.Assessment,
        labelResId = R.string.nav_reports,
        contentDescriptionResId = R.string.cd_reports_nav,
    )

    /** Health tab — stub for future features. */
    data object Health : NavRoute(
        route = ROUTE_HEALTH,
        icon = Icons.Filled.HealthAndSafety,
        labelResId = R.string.nav_health,
        contentDescriptionResId = R.string.cd_health_nav,
    )

    // ── Non-Tab Destinations ────────────────────────────────────────

    /** Design wizard (reached from Home Screen for New Installation projects). */
    data object Wizard : NavRoute(
        route = "$ROUTE_WIZARD/{$ARG_PROJECT_ID}",
        icon = Icons.Filled.AutoAwesome,
        labelResId = R.string.nav_wizard,
        contentDescriptionResId = R.string.cd_wizard_nav,
    )

    /** Dashboard (reached from Home Screen for Existing Assessment projects). */
    data object Dashboard : NavRoute(
        route = "$ROUTE_DASHBOARD/{$ARG_PROJECT_ID}",
        icon = Icons.Filled.Dashboard,
        labelResId = R.string.nav_dashboard,
        contentDescriptionResId = R.string.cd_dashboard_nav,
    )

    /** Catalog — legacy route retained for future features. */
    data object Catalog : NavRoute(
        route = ROUTE_CATALOG,
        icon = Icons.Filled.Inventory2,
        labelResId = R.string.nav_catalog,
        contentDescriptionResId = R.string.cd_catalog_nav,
    )

    /** Settings — legacy route retained for future features. */
    data object Settings : NavRoute(
        route = ROUTE_SETTINGS,
        icon = Icons.Filled.Settings,
        labelResId = R.string.nav_settings,
        contentDescriptionResId = R.string.cd_settings_nav,
    )

    companion object {
        const val ARG_PROJECT_ID = "projectId"

        private const val ROUTE_PROJECTS = "projects"
        private const val ROUTE_DESIGN = "design"
        private const val ROUTE_REPORTS = "reports"
        private const val ROUTE_HEALTH = "health"
        private const val ROUTE_WIZARD = "wizard"
        private const val ROUTE_DASHBOARD = "dashboard"
        private const val ROUTE_CATALOG = "catalog"
        private const val ROUTE_SETTINGS = "settings"

        /** Routes displayed in the bottom navigation bar (FR-017). */
        val bottomNavItems = listOf(Projects, Design, Reports, Health)

        /** Constructs a wizard route with the given [projectId]. */
        fun wizardRoute(projectId: String): String = "$ROUTE_WIZARD/$projectId"

        /** Constructs a dashboard route with the given [projectId]. */
        fun dashboardRoute(projectId: String): String = "$ROUTE_DASHBOARD/$projectId"
    }
}
