package com.poultry.broiler.util

sealed class SeedLoadResult {
    data object Success : SeedLoadResult()
    data class Error(
        val exception: Exception,
        val retryCount: Int,
    ) : SeedLoadResult()
}

/**
 * Error messages for seed data load failures.
 * After 2 failed retries, the user is prompted to reinstall the application.
 */
object SeedErrorMessages {
    const val LOAD_FAILED = "Échec du chargement des données initiales."
    const val REINSTALL = "Veuillez réinstaller l'application"
    const val RETRY = "Réessayer"
}
