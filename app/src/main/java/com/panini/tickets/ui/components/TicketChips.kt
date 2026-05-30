package com.panini.tickets.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.panini.tickets.core.UserMessages
import com.panini.tickets.domain.model.TicketCategory
import com.panini.tickets.domain.model.TicketPriority
import com.panini.tickets.domain.model.TicketStatus
import com.panini.tickets.ui.theme.AppPrimary
import com.panini.tickets.ui.theme.AppSecondaryText

fun TicketPriority.label(): String = when (this) {
    TicketPriority.CRITICAL -> UserMessages.Priority.CRITICAL
    TicketPriority.HIGH     -> UserMessages.Priority.HIGH
    TicketPriority.MEDIUM   -> UserMessages.Priority.MEDIUM
    TicketPriority.LOW      -> UserMessages.Priority.LOW
}

fun TicketStatus.label(): String = when (this) {
    TicketStatus.OPEN        -> UserMessages.Status.OPEN
    TicketStatus.IN_PROGRESS -> UserMessages.Status.IN_PROGRESS
    TicketStatus.RESOLVED    -> UserMessages.Status.RESOLVED
    TicketStatus.CLOSED      -> UserMessages.Status.CLOSED
}

fun TicketCategory.label(): String = when (this) {
    TicketCategory.SUPPLIER     -> UserMessages.Category.SUPPLIER
    TicketCategory.INVENTORY    -> UserMessages.Category.INVENTORY
    TicketCategory.DISTRIBUTION -> UserMessages.Category.DISTRIBUTION
    TicketCategory.LOGISTICS    -> UserMessages.Category.LOGISTICS
    TicketCategory.PACKAGING    -> UserMessages.Category.PACKAGING
    TicketCategory.OTHER        -> UserMessages.Category.OTHER
}

private fun TicketPriority.color(): Color = when (this) {
    TicketPriority.CRITICAL -> Color(0xFFCF6679)
    TicketPriority.HIGH     -> Color(0xFFE0A458)
    TicketPriority.MEDIUM   -> AppPrimary
    TicketPriority.LOW      -> AppSecondaryText
}

private fun TicketStatus.color(): Color = when (this) {
    TicketStatus.OPEN        -> AppPrimary
    TicketStatus.IN_PROGRESS -> Color(0xFFE0A458)
    TicketStatus.RESOLVED    -> Color(0xFF6FBF73)
    TicketStatus.CLOSED      -> AppSecondaryText
}

@Composable
fun PriorityChip(priority: TicketPriority, modifier: Modifier = Modifier) {
    ColoredChip(text = priority.label(), color = priority.color(), modifier = modifier)
}

@Composable
fun StatusChip(status: TicketStatus, modifier: Modifier = Modifier) {
    ColoredChip(text = status.label(), color = status.color(), modifier = modifier)
}

@Composable
private fun ColoredChip(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier
            .background(color = color.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
