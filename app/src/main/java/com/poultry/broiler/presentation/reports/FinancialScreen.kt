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
    var flockCycles by remember { mutableStateOf(5) }
    var upgradeProposals by remember {
        mutableStateOf(
            listOf(
                UpgradeProposal(
                    id = 1,
                    title = "Isolation de toiture haute perf.",
                    priority = UpgradePriority.CRITICAL,
                    currentState = "R-12 Polystyrène (vétuste)",
                    proposedState = "R-28 Polyuréthane projeté",
                    cost = "450,000 DZD",
                    payback = "1.5 ans",
                ),
                UpgradeProposal(
                    id = 2,
                    title = "Ventilation dynamique EC Fan",
                    priority = UpgradePriority.HIGH,
                    currentState = "10x Ventilateurs standard 1.5 CV",
                    proposedState = "8x EC Fan progressif 1.2 CV",
                    cost = "600,000 DZD",
                    payback = "2.1 ans",
                ),
                UpgradeProposal(
                    id = 3,
                    title = "Éclairage LED à intensité variable",
                    priority = UpgradePriority.MEDIUM,
                    currentState = "Néons fluorescents",
                    proposedState = "LED 20 lux gradable",
                    cost = "180,000 DZD",
                    payback = "0.8 ans",
                ),
            ),
        )
    }

    // Calculations
    val annualProfit = flockCycles * baseProfitPerCycle
    val formattedCapex = formatDzd(baseCapex)
    val formattedOpex = "${formatDzd(baseOpexPerCycle)} / cycle"
    val formattedProfit = "${formatDzd(annualProfit)} / an"

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Text(
            text = "Analyse & Simulation Financière",
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
            text = "Améliorations Techniques Recommandées",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        // Recommendations List
        upgradeProposals.forEach { proposal ->
            UpgradeRecommendationCard(
                proposal = proposal,
                onToggled = { isSelected ->
                    upgradeProposals =
                        upgradeProposals.map { item ->
                            if (item.id == proposal.id) item.copy(isSelected = isSelected) else item
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
