package com.poultry.broiler.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poultry.broiler.presentation.theme.DialogCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryElevation
import com.poultry.broiler.presentation.theme.PoultryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val spacing = LocalSpacing.current

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        shape =
            RoundedCornerShape(
                topStart = DialogCornerRadius,
                topEnd = DialogCornerRadius,
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
            ),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = PoultryElevation.modal,
        dragHandle = {},
        modifier = modifier,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.medium)
                        .padding(top = spacing.large),
            )
        }
        content()
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetPreview() {
    PoultryTheme {
        val spacing = LocalSpacing.current
        BottomSheet(title = "Détails") {
            Text(
                text = "Contenu de la fiche",
                modifier = Modifier.padding(spacing.medium),
            )
        }
    }
}
