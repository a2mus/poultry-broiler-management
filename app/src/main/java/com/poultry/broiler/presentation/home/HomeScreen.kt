package com.poultry.broiler.presentation.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.screen_home),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
    )
}
