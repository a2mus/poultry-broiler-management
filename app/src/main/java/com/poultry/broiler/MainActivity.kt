package com.poultry.broiler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.poultry.broiler.presentation.navigation.MainNavigation
import com.poultry.broiler.presentation.theme.PoultryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoultryTheme {
                MainNavigation()
            }
        }
    }
}
