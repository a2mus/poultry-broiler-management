package com.poultry.broiler.presentation.home

/**
 * One-shot navigation events emitted by [HomeViewModel].
 *
 * Consumed via [HomeViewModel.navigationEvent] Flow in [HomeScreen].
 */
sealed interface HomeNavigation {
    /** Navigate to the design wizard for a New Installation project. */
    data class ToWizard(val projectId: String) : HomeNavigation

    /** Navigate to the dashboard for an Existing Assessment project. */
    data class ToDashboard(val projectId: String) : HomeNavigation
}
