package com.poultry.broiler.presentation.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poultry.broiler.R

/**
 * Confirmation dialog before permanent project deletion (FR-012).
 *
 * Shows the project name in bold and styles the confirm button in the error
 * colour to convey the destructive nature (Constitution Art 3.3 — critical
 * warnings ≥ 7:1 contrast).
 *
 * @param projectName The name shown in the warning message.
 * @param onConfirm Called when the user confirms deletion.
 * @param onDismiss Called when the user cancels.
 */
@Composable
fun DeleteConfirmationDialog(
    projectName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text(stringResource(R.string.dialog_delete_title)) },
        text = {
            Text(
                text = stringResource(R.string.dialog_delete_message, projectName),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors =
                    androidx.compose.material3.ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.action_delete),
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
}
