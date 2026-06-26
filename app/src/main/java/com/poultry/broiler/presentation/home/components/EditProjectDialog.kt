package com.poultry.broiler.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.usecase.CreateProjectUseCase
import com.poultry.broiler.presentation.theme.LocalSpacing

/**
 * Edit dialog for updating a project's name and location (FR-010).
 *
 * The project type is shown as read-only text (immutable after creation).
 * Fields are pre-filled with the current values.
 *
 * @param project The project being edited (pre-fills the fields).
 * @param onDismiss Called when the dialog is cancelled.
 * @param onConfirm Called with the updated name and optional location.
 */
@Composable
fun EditProjectDialog(
    project: Project,
    onDismiss: () -> Unit,
    onConfirm: (name: String, location: String?) -> Unit,
) {
    val spacing = LocalSpacing.current
    var name by remember { mutableStateOf(project.name) }
    var location by remember { mutableStateOf(project.location.orEmpty()) }

    val trimmedName = name.trim()
    val isNameBlank = trimmedName.isEmpty()
    val isNameTooLong = name.length > CreateProjectUseCase.MAX_NAME_LENGTH
    val isLocationTooLong = location.length > CreateProjectUseCase.MAX_LOCATION_LENGTH
    val isFormValid = !isNameBlank && !isNameTooLong && !isLocationTooLong

    val nameError: String? =
        when {
            isNameBlank -> stringResource(R.string.error_name_required)
            isNameTooLong -> stringResource(R.string.error_name_too_long)
            else -> null
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_edit_project_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.dialog_new_project_name)) },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next,
                        ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Column(modifier = Modifier.padding(top = spacing.xxs)) {
                    Text(
                        text = stringResource(R.string.dialog_edit_project_type_readonly),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = project.type.displayNameFr,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(stringResource(R.string.dialog_new_project_location)) },
                    isError = isLocationTooLong,
                    supportingText =
                        if (isLocationTooLong) {
                            { Text(stringResource(R.string.error_location_too_long)) }
                        } else {
                            null
                        },
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
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
                    onConfirm(trimmedName, locationValue)
                },
                enabled = isFormValid,
            ) {
                Text(stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
}
