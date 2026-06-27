@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poultry.broiler.R
import com.poultry.broiler.presentation.catalog.components.UpgradePriority
import com.poultry.broiler.presentation.catalog.components.UpgradeProposal
import com.poultry.broiler.presentation.catalog.components.UpgradeRecommendationCard
import com.poultry.broiler.presentation.reports.components.FinancialSummaryGrid
import com.poultry.broiler.presentation.reports.components.FlockCyclesSimulator
import com.poultry.broiler.presentation.reports.components.RoiPaybackChart
import com.poultry.broiler.presentation.theme.LocalSpacing
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinancialScreen(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    // Financial base constants
    val baseCapex = 4200000.0
    val baseOpexPerCycle = 1800000.0
    val baseProfitPerCycle = 1200000.0

    // Simulation states
    // Simulation states
    var flockCycles by remember { mutableStateOf(5) }
    var selectedUpgrades by remember { mutableStateOf(setOf<Int>()) }

    val p1Title = stringResource(R.string.upgrade_1_title)
    val p1Current = stringResource(R.string.upgrade_1_current)
    val p1Proposed = stringResource(R.string.upgrade_1_proposed)
    val p1Cost = stringResource(R.string.upgrade_1_cost)
    val p1Payback = stringResource(R.string.upgrade_1_payback)

    val p2Title = stringResource(R.string.upgrade_2_title)
    val p2Current = stringResource(R.string.upgrade_2_current)
    val p2Proposed = stringResource(R.string.upgrade_2_proposed)
    val p2Cost = stringResource(R.string.upgrade_2_cost)
    val p2Payback = stringResource(R.string.upgrade_2_payback)

    val p3Title = stringResource(R.string.upgrade_3_title)
    val p3Current = stringResource(R.string.upgrade_3_current)
    val p3Proposed = stringResource(R.string.upgrade_3_proposed)
    val p3Cost = stringResource(R.string.upgrade_3_cost)
    val p3Payback = stringResource(R.string.upgrade_3_payback)

    val upgradeProposals = remember(
        p1Title, p1Current, p1Proposed, p1Cost, p1Payback,
        p2Title, p2Current, p2Proposed, p2Cost, p2Payback,
        p3Title, p3Current, p3Proposed, p3Cost, p3Payback,
        selectedUpgrades
    ) {
        listOf(
            UpgradeProposal(1, p1Title, UpgradePriority.CRITICAL, p1Current, p1Proposed, p1Cost, p1Payback, 1 in selectedUpgrades),
            UpgradeProposal(2, p2Title, UpgradePriority.HIGH, p2Current, p2Proposed, p2Cost, p2Payback, 2 in selectedUpgrades),
            UpgradeProposal(3, p3Title, UpgradePriority.MEDIUM, p3Current, p3Proposed, p3Cost, p3Payback, 3 in selectedUpgrades)
        )
    }

    // Calculations
    val annualProfit = flockCycles * baseProfitPerCycle
    val formattedCapex = formatDzd(baseCapex)
    val formattedOpex = stringResource(R.string.financial_opex_format, formatDzd(baseOpexPerCycle))
    val formattedProfit = stringResource(R.string.financial_profit_format, formatDzd(annualProfit))

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Text(
            text = stringResource(R.string.financial_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        // Summary Bento Grid
        FinancialSummaryGrid(
            capex = formattedCapex,
            opex = formattedOpex,
            netProfit = formattedProfit,
        )

        // Dynamic cycles simulator slider
        FlockCyclesSimulator(
            currentCycles = flockCycles,
            onCyclesChanged = { flockCycles = it },
        )

        // Area ROI chart
        RoiPaybackChart(
            annualProfit = annualProfit,
            capex = baseCapex,
        )

        Spacer(modifier = Modifier.height(spacing.xxs))

        Text(
            text = stringResource(R.string.financial_upgrades_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        // Recommendations List
        upgradeProposals.forEach { proposal ->
            UpgradeRecommendationCard(
                proposal = proposal,
                onToggled = { isSelected ->
                    selectedUpgrades = if (isSelected) {
                        selectedUpgrades + proposal.id
                    } else {
                        selectedUpgrades - proposal.id
                    }
                },
            )
        }
    }
}

private fun formatDzd(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.FRANCE)
    return "${formatter.format(amount.toInt())} DZD"
}
