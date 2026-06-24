package com.poultry.broiler.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.poultry.broiler.presentation.catalog.CatalogScreen
import com.poultry.broiler.presentation.dashboard.DashboardScreen
import com.poultry.broiler.presentation.home.HomeScreen
import com.poultry.broiler.presentation.settings.SettingsScreen
import com.poultry.broiler.presentation.wizard.WizardScreen

@Composable
fun PoultryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route,
        modifier = modifier,
    ) {
        composable(NavRoute.Home.route) {
            HomeScreen()
        }
        composable(NavRoute.Wizard.route) {
            WizardScreen()
        }
        composable(NavRoute.Dashboard.route) {
            DashboardScreen()
        }
        composable(NavRoute.Catalog.route) {
            CatalogScreen()
        }
        composable(NavRoute.Settings.route) {
            SettingsScreen()
        }
    }
}
