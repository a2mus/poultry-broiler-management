package com.poultry.broiler

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Poultry Broiler Management.
 *
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation,
 * including a base class for dependency injection in the application.
 */
@HiltAndroidApp
class PoultryApp : Application()
