package com.panini.tickets.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panini.tickets.core.FeatureFlags
import com.panini.tickets.core.UserMessages
import com.panini.tickets.di.AppContainer
import com.panini.tickets.domain.model.TicketPriority
import com.panini.tickets.domain.model.TicketStatus
import com.panini.tickets.ui.components.PriorityChip
import com.panini.tickets.ui.components.StatusChip
import com.panini.tickets.ui.components.label
import com.panini.tickets.ui.theme.AppPrimary
import com.panini.tickets.ui.theme.AppSecondaryText

@Composable
fun TicketDetailScreen(
    ticketId: Int,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TicketDetailViewModel = viewModel(
        factory = TicketDetailViewModelFactory(AppContainer.ticketRepository, ticketId)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showStatusDialog by remember { mutableStateOf(false) }
    var showPriorityDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { snackbarHostState.showSnackbar(it) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        val ticket = uiState.ticket
        when {
            uiState.isLoading && ticket == null -> CenterText(UserMessages.Tickets.LOADING)

            uiState.error != null && ticket == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error!!,
                        color = AppSecondaryText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    TextButton(onClick = { viewModel.load() }) {
                        Text(text = UserMessages.TAP_TO_RETRY, color = AppPrimary)
                    }
                }
            }

            ticket != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = ticket.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PriorityChip(priority = ticket.priority)
                        StatusChip(status = ticket.status)
                    }
                    DetailField(UserMessages.TicketDetail.DESCRIPTION_LABEL, ticket.description)
                    DetailField(UserMessages.TicketDetail.SUPPLIER_LABEL, ticket.supplier)
                    DetailField(UserMessages.TicketDetail.CATEGORY_LABEL, ticket.category.label())
                    DetailField(UserMessages.TicketDetail.CREATED_AT_LABEL, ticket.createdAt)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showStatusDialog = true },
                        enabled = !uiState.isUpdating,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(UserMessages.TicketDetail.CHANGE_STATUS)
                    }

                    // FEATURE FLAG: hides the priority-change button when
                    // ENABLE_PRIORITY_CHANGE is false. The underlying ViewModel
                    // logic is unchanged; only the UI entry point is gated.
                    if (FeatureFlags.ENABLE_PRIORITY_CHANGE) {
                        OutlinedButton(
                            onClick = { showPriorityDialog = true },
                            enabled = !uiState.isUpdating,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(UserMessages.TicketDetail.CHANGE_PRIORITY)
                        }
                    }
                }

                if (showStatusDialog) {
                    OptionsDialog(
                        title = UserMessages.TicketDetail.CHANGE_STATUS,
                        options = TicketStatus.entries,
                        selected = ticket.status,
                        labelOf = { it.label() },
                        onConfirm = {
                            viewModel.updateStatus(it)
                            showStatusDialog = false
                        },
                        onDismiss = { showStatusDialog = false }
                    )
                }

                if (showPriorityDialog) {
                    OptionsDialog(
                        title = UserMessages.TicketDetail.CHANGE_PRIORITY,
                        options = TicketPriority.entries,
                        selected = ticket.priority,
                        labelOf = { it.label() },
                        onConfirm = {
                            viewModel.updatePriority(it)
                            showPriorityDialog = false
                        },
                        onDismiss = { showPriorityDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = AppSecondaryText
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun <T> OptionsDialog(
    title: String,
    options: List<T>,
    selected: T,
    labelOf: (T) -> String,
    onConfirm: (T) -> Unit,
    onDismiss: () -> Unit
) {
    var choice by remember { mutableStateOf(selected) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected = option == choice, onClick = { choice = option })
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(selected = option == choice, onClick = { choice = option })
                        Text(text = labelOf(option), color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(choice) }) {
                Text(text = UserMessages.Common.SAVE, color = AppPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = UserMessages.Common.CANCEL, color = AppSecondaryText)
            }
        }
    )
}

@Composable
private fun CenterText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = AppSecondaryText, style = MaterialTheme.typography.bodyLarge)
    }
}
