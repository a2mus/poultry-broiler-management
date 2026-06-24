package com.poultry.broiler.presentation.wizard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poultry.broiler.R
import com.poultry.broiler.presentation.wizard.components.WizardNavigationBar
import com.poultry.broiler.presentation.wizard.components.WizardStepIndicator

/**
 * Container for the House Dimensions Wizard.
 *
 * Implements the wizard shell defined in `contracts/composables.md`: a Scaffold
 * with the step indicator at the top, the step composable in the content area,
 * and the navigation bar at the bottom. The current implementation renders
 * [HouseDimensionsStep] whenever [WizardUiState.Active.currentStep] equals 1;
 * future wizard steps (#004–#006) will add additional `when` branches.
 *
 * @param projectId Project identifier read from the `wizard/{projectId}` route.
 * @param onNavigateBack Invoked when the system back button is pressed on Step 1.
 */
@Composable
fun WizardScreen(
    projectId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WizardViewModel = hiltViewModel(),
) {
    LaunchedEffect(projectId) {
        viewModel.load(projectId)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val wizardSaveError = stringResource(R.string.wizard_save_error)

    LaunchedEffect(state) {
        val active = state as? WizardUiState.Active ?: return@LaunchedEffect
        if (active.saveError != null) {
            snackbarHostState.showSnackbar(
                message = wizardSaveError,
                actionLabel = null,
                withDismissAction = true,
            )
        }
    }

    when (state) {
        WizardUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "…",
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        is WizardUiState.Error -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error,
            )
        }
        is WizardUiState.Active -> {
            val active = state
            // If the user navigates back to Step 1 from a future step
            // (currently unreachable; closed scaffolding for forward-compat),
            // we surface the system back handler.
            LaunchedEffect(active.currentStep) {
                if (active.currentStep <= 1) {
                    runCatching { onNavigateBack }
                }
            }
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    WizardStepIndicator(
                        currentStep = active.currentStep,
                        totalSteps = active.totalSteps,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                bottomBar = {
                    WizardNavigationBar(
                        canGoNext = active.canGoNext,
                        canGoPrevious = active.canGoPrevious,
                        onNext = { viewModel.onIntent(WizardIntent.GoNext, projectId) },
                        onPrevious = { viewModel.onIntent(WizardIntent.GoPrevious, projectId) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
            ) { padding ->
                when (active.currentStep) {
                    1 -> HouseDimensionsStep(
                        formState = active.dimensions,
                        onIntent = { intent -> viewModel.onIntent(intent, projectId) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                    )
                    else -> PlaceholderStep(contentPadding = padding)
                }
            }
        }
    }
}

@Composable
private fun PlaceholderStep(contentPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Étape à venir",
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}