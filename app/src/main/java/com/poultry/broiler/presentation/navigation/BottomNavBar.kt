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

/**
 * Bottom navigation bar with four tabs: Projects, Design, Reports, Health (FR-017).
 *
 * The Projects tab is the primary, fully-functional destination; the other
 * three are stubs for future features.
 *
 * @param currentRoute The currently active route, used to highlight the selected tab.
 * @param onNavigate Callback invoked when a tab is tapped.
 */
@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (NavRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        NavRoute.bottomNavItems.forEach { navItem ->
            val cd = stringResource(navItem.contentDescriptionResId)
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = { onNavigate(navItem) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = cd,
                    )
                },
                label = { Text(stringResource(navItem.labelResId)) },
                modifier =
                    Modifier.semantics {
                        contentDescription = cd
                    },
            )
        }
    }
}
