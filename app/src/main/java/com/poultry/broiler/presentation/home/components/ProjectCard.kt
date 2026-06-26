package com.poultry.broiler.presentation.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.model.ProjectStatus
import com.poultry.broiler.domain.model.ProjectType
import com.poultry.broiler.presentation.theme.BadgeCornerRadius
import com.poultry.broiler.presentation.theme.CardCornerRadius
import com.poultry.broiler.presentation.theme.LocalSpacing
import com.poultry.broiler.presentation.theme.PoultryElevation
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Rich card displaying project metadata in the bento grid (FR-003).
 *
 * Supports single-tap to open and long-press to reveal a context menu with
 * a scale-down micro-interaction (FR-009).
 *
 * @param project Domain model to display.
 * @param onClick Tap handler (navigates to wizard or dashboard).
 * @param onLongClick Long-press handler (opens context menu).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "cardScale",
    )

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .scale(scale)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = {
                        isPressed = true
                        onLongClick()
                    },
                ),
        elevation = CardDefaults.cardElevation(defaultElevation = PoultryElevation.card),
        shape = RoundedCornerShape(CardCornerRadius),
    ) {
        Column(modifier = Modifier.padding(spacing.md)) {
            TypeBadge(type = project.type)

            Spacer(modifier = Modifier.height(spacing.sm))

            Text(
                text = project.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (!project.location.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(spacing.xxs))
                MetadataRow(
                    icon = Icons.Filled.LocationOn,
                    text = project.location,
                )
            }

            Spacer(modifier = Modifier.height(spacing.xxs))
            MetadataRow(
                icon = Icons.Filled.CalendarMonth,
                text = project.createdAt.toFormattedDate(),
            )

            Spacer(modifier = Modifier.height(spacing.sm))
            StatusIndicator(status = project.status)

            Spacer(modifier = Modifier.height(spacing.md))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.home_capacity),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                ComplianceIndicator()
            }
        }
    }
}

@Composable
private fun MetadataRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    val spacing = LocalSpacing.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(spacing.xxs))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TypeBadge(type: ProjectType) {
    val spacing = LocalSpacing.current
    val (containerColor, contentColor) =
        when (type) {
            ProjectType.NEW_INSTALLATION ->
                MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
            ProjectType.EXISTING_ASSESSMENT ->
                MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        }

    val cdRes =
        when (type) {
            ProjectType.NEW_INSTALLATION -> R.string.cd_type_badge_new
            ProjectType.EXISTING_ASSESSMENT -> R.string.cd_type_badge_existing
        }
    val badgeContentDescription = stringResource(cdRes)

    Box(
        modifier =
            Modifier
                .clip(RoundedCornerShape(BadgeCornerRadius))
                .background(containerColor)
                .padding(horizontal = spacing.sm, vertical = spacing.xxs)
                .semantics { contentDescription = badgeContentDescription },
    ) {
        Text(
            text = type.badgeText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor,
        )
    }
}

@Composable
private fun StatusIndicator(status: ProjectStatus) {
    val spacing = LocalSpacing.current
    val dotColor =
        when (status) {
            ProjectStatus.DRAFT -> MaterialTheme.colorScheme.outline
            ProjectStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
            ProjectStatus.COMPLETED -> MaterialTheme.colorScheme.tertiary
        }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor),
        )
        Spacer(modifier = Modifier.width(spacing.xxs))
        Text(
            text = status.displayNameFr,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ComplianceIndicator() {
    val circleColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier =
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(circleColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.home_not_applicable),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun Long.toFormattedDate(): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter =
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}
