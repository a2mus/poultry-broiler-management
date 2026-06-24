package com.poultry.broiler.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.poultry.broiler.presentation.catalog.CatalogScreen
import com.poultry.broiler.presentation.dashboard.DashboardScreen
import com.poultry.broiler.presentation.design.DesignScreen
import com.poultry.broiler.presentation.health.HealthScreen
import com.poultry.broiler.presentation.home.HomeScreen
import com.poultry.broiler.presentation.reports.ReportsScreen
import com.poultry.broiler.presentation.settings.SettingsScreen
import com.poultry.broiler.presentation.wizard.WizardScreen

/**
 * Compose Navigation host wiring all application destinations.
 *
 * The [NavRoute.Projects] tab hosts [HomeScreen] and connects its navigation
 * events to the wizard (New Installation) and dashboard (Existing Assessment)
 * routes (FR-007, FR-008).
 */
@Composable
fun PoultryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Projects.route,
        modifier = modifier,
    ) {
        // ── Bottom Navigation Tabs ──────────────────────────────

        composable(NavRoute.Projects.route) {
            HomeScreen(
                onNavigateToWizard = { projectId ->
                    navController.navigate(NavRoute.wizardRoute(projectId))
                },
                onNavigateToDashboard = { projectId ->
                    navController.navigate(NavRoute.dashboardRoute(projectId))
                },
            )
        }

        composable(NavRoute.Design.route) {
            DesignScreen()
        }

        composable(NavRoute.Reports.route) {
            ReportsScreen()
        }

        composable(NavRoute.Health.route) {
            HealthScreen()
        }

        // ── Non-Tab Destinations ────────────────────────────────

        composable(
            route = NavRoute.Wizard.route,
            arguments = listOf(
                navArgument(NavRoute.ARG_PROJECT_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments
                ?.getString(NavRoute.ARG_PROJECT_ID).orEmpty()
            WizardScreen(
                projectId = projectId,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            route = NavRoute.Dashboard.route,
            arguments = listOf(
                navArgument(NavRoute.ARG_PROJECT_ID) { type = NavType.StringType },
            ),
        ) {
            DashboardScreen()
        }

        // ── Legacy Routes ───────────────────────────────────────

        composable(NavRoute.Catalog.route) {
            CatalogScreen()
        }

        composable(NavRoute.Settings.route) {
            SettingsScreen()
        }
    }
}
