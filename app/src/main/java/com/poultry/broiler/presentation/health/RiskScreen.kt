@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poultry.broiler.R
import com.poultry.broiler.presentation.health.components.RiskScoreGauge
import com.poultry.broiler.presentation.health.components.SensorGrid
import com.poultry.broiler.presentation.health.components.WelfareCheckItem
import com.poultry.broiler.presentation.health.components.WelfareChecklist
import com.poultry.broiler.presentation.theme.LocalSpacing

@Composable
fun RiskScreen(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    // Local checklist items state representing EU criteria
    var checklistItems by remember {
        mutableStateOf(
            listOf(
                WelfareCheckItem(1, R.string.welfare_item_1, true),
                WelfareCheckItem(2, R.string.welfare_item_2, true),
                WelfareCheckItem(3, R.string.welfare_item_3, false),
                WelfareCheckItem(4, R.string.welfare_item_4, true),
                WelfareCheckItem(5, R.string.welfare_item_5, false),
            ),
        )
    }

    // Dynamic compliance score recalculation
    val score =
        remember(checklistItems) {
            val checkedCount = checklistItems.count { it.isChecked }
            val totalCount = checklistItems.size
            if (totalCount > 0) ((checkedCount.toFloat() / totalCount) * 100).toInt() else 0
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Radial progress gauge
        RiskScoreGauge(score = score)

        Spacer(modifier = Modifier.height(spacing.xs))

        // Environmental telemetry grid
        SensorGrid()

        Spacer(modifier = Modifier.height(spacing.xs))

        // Interactive compliance checklist
        WelfareChecklist(
            items = checklistItems,
            onItemToggled = { itemId, isChecked ->
                checklistItems =
                    checklistItems.map { item ->
                        if (item.id == itemId) item.copy(isChecked = isChecked) else item
                    }
            },
        )
    }
}
