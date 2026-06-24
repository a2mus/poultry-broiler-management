package com.poultry.broiler.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.ProjectType
import com.poultry.broiler.domain.usecase.CreateProjectUseCase
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Creation dialog for a new project (FR-002).
 *
 * Collects the project name (required), type via segmented buttons, and an
 * optional location. The confirm button is disabled until the name is valid.
 *
 * @param onDismiss Called when the dialog is cancelled.
 * @param onConfirm Called with the validated name, type, and optional location.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, type: ProjectType, location: String?) -> Unit,
) {
    val spacing = LocalSpacing.current
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ProjectType.NEW_INSTALLATION) }
    var location by remember { mutableStateOf("") }
    var nameHasInteracted by remember { mutableStateOf(false) }

    val trimmedName = name.trim()
    val isNameBlank = trimmedName.isEmpty()
    val isNameTooLong = name.length > CreateProjectUseCase.MAX_NAME_LENGTH
    val isLocationTooLong = location.length > CreateProjectUseCase.MAX_LOCATION_LENGTH
    val isFormValid = !isNameBlank && !isNameTooLong && !isLocationTooLong

    val nameError: String? = when {
        nameHasInteracted && isNameBlank -> stringResource(R.string.error_name_required)
        isNameTooLong -> stringResource(R.string.error_name_too_long)
        else -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_new_project_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameHasInteracted = true
                    },
                    label = { Text(stringResource(R.string.dialog_new_project_name)) },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Column {
                    Text(
                        text = stringResource(R.string.dialog_new_project_type),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = spacing.xxs),
                    )
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = type == ProjectType.NEW_INSTALLATION,
                            onClick = { type = ProjectType.NEW_INSTALLATION },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            label = { Text(text = stringResource(R.string.dialog_new_project_type_new)) },
                        )
                        SegmentedButton(
                            selected = type == ProjectType.EXISTING_ASSESSMENT,
                            onClick = { type = ProjectType.EXISTING_ASSESSMENT },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            label = { Text(text = stringResource(R.string.dialog_new_project_type_existing)) },
                        )
                    }
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(stringResource(R.string.dialog_new_project_location)) },
                    isError = isLocationTooLong,
                    supportingText = if (isLocationTooLong) {
                        { Text(stringResource(R.string.error_location_too_long)) }
                    } else {
                        null
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val locationValue = location.trim().ifEmpty { null }
                    onConfirm(trimmedName, type, locationValue)
                },
                enabled = isFormValid,
            ) {
                Text(stringResource(R.string.action_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
}
