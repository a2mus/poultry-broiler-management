package com.poultry.broiler.presentation.home.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R

/**
 * Context menu shown on long-press of a project card.
 *
 * Offers Edit, Duplicate, and Delete actions. The Delete item is styled
 * in the error colour to signal its destructive nature (Constitution Art 3.3).
 *
 * @param expanded Whether the menu is visible.
 * @param onDismiss Called when the menu should close.
 * @param onEdit Edit action callback.
 * @param onDuplicate Duplicate action callback.
 * @param onDelete Delete action callback.
 */
@Composable
fun ProjectContextMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.menu_edit)) },
            onClick = {
                onDismiss()
                onEdit()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.menu_duplicate)) },
            onClick = {
                onDismiss()
                onDuplicate()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.menu_delete),
                    color = MaterialTheme.colorScheme.error,
                )
            },
            onClick = {
                onDismiss()
                onDelete()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp),
                )
            },
        )
    }
}
