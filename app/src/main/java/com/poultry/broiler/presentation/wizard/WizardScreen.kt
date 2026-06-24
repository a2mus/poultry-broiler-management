package com.poultry.broiler.presentation.wizard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R

@Composable
fun WizardScreen(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.screen_wizard),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
    )
}
