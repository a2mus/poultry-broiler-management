@file:Suppress("LongMethod", "MagicNumber")

package com.poultry.broiler.presentation.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing

enum class UpgradePriority {
    CRITICAL,
    HIGH,
    MEDIUM,
}

data class UpgradeProposal(
    val id: Int,
    val title: String,
    val priority: UpgradePriority,
    val currentState: String,
    val proposedState: String,
    val cost: String,
    val payback: String,
    val isSelected: Boolean = false,
)

@Composable
fun UpgradeRecommendationCard(
    proposal: UpgradeProposal,
    onToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    val priorityColor =
        when (proposal.priority) {
            UpgradePriority.CRITICAL -> Color(0xFFE74C3C)
            UpgradePriority.HIGH -> Color(0xFFF39C12)
            UpgradePriority.MEDIUM -> Color(0xFF2ECC71)
        }

    val priorityText =
        when (proposal.priority) {
            UpgradePriority.CRITICAL -> "CRITIQUE"
            UpgradePriority.HIGH -> "ÉLEVÉ"
            UpgradePriority.MEDIUM -> "MOYEN"
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left priority highlight border
            Box(
                modifier =
                    Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                        .height(130.dp) // Fixed height to cover the card vertically
                        .background(priorityColor),
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(spacing.md),
            ) {
                // Header (Title & Priority Badge)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = proposal.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Box(
                        modifier =
                            Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(priorityColor.copy(alpha = 0.15f))
                                .padding(horizontal = spacing.xs, vertical = spacing.xxs),
                    ) {
                        Text(
                            text = priorityText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityColor,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(spacing.sm))

                // Side-by-side comparison boxes
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ComparisonBox(
                        label = "Actuel",
                        value = proposal.currentState,
                        modifier = Modifier.weight(1f),
                    )
                    ComparisonBox(
                        label = "Proposé",
                        value = proposal.proposedState,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.height(spacing.sm))

                // Footer (Cost, Payback, and Toggle Switch)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Text(
                            text = "Coût: ${proposal.cost} | ROI: ${proposal.payback}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = proposal.isSelected,
                        onCheckedChange = onToggled,
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
    ) {
        Column(modifier = Modifier.padding(spacing.sm)) {
            Text(
                text = label,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(spacing.xxs))
            Text(
                text = value,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
