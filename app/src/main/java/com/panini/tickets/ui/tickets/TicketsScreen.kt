package com.panini.tickets.ui.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panini.tickets.core.UserMessages
import com.panini.tickets.di.AppContainer
import com.panini.tickets.domain.model.Ticket
import com.panini.tickets.ui.components.TicketCard
import com.panini.tickets.ui.theme.AppPrimary
import com.panini.tickets.ui.theme.AppSecondaryText

@Composable
fun TicketsScreen(
    onTicketClick: (Ticket) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketsViewModel = viewModel(
        factory = TicketsViewModelFactory(AppContainer.ticketRepository)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.tickets.isEmpty() -> {
                CenterText(UserMessages.Tickets.LOADING)
            }

            uiState.error != null && uiState.tickets.isEmpty() -> {
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
                    TextButton(onClick = { viewModel.fetchTickets() }) {
                        Text(text = UserMessages.TAP_TO_RETRY, color = AppPrimary)
                    }
                }
            }

            uiState.tickets.isEmpty() -> {
                CenterText(UserMessages.Tickets.EMPTY)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = uiState.tickets, key = { it.ticketId }) { ticket ->
                        TicketCard(ticket = ticket, onClick = { onTicketClick(ticket) })
                    }
                }
            }
        }
    }
}

@Composable
private fun CenterText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = AppSecondaryText, style = MaterialTheme.typography.bodyLarge)
    }
}
