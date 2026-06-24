package com.poultry.broiler.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (NavRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        NavRoute.items.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = { onNavigate(navItem) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = stringResource(
                            when (navItem) {
                                is NavRoute.Home -> com.poultry.broiler.R.string.cd_home_nav
                                is NavRoute.Wizard -> com.poultry.broiler.R.string.cd_wizard_nav
                                is NavRoute.Dashboard -> com.poultry.broiler.R.string.cd_dashboard_nav
                                is NavRoute.Catalog -> com.poultry.broiler.R.string.cd_catalog_nav
                                is NavRoute.Settings -> com.poultry.broiler.R.string.cd_settings_nav
                            },
                        ),
                    )
                },
                label = { Text(stringResource(navItem.labelResId)) },
            )
        }
    }
}
