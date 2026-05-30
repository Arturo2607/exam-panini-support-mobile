package com.panini.tickets.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.tickets.core.event.TicketEventBus
import com.panini.tickets.data.remote.ApiResult
import com.panini.tickets.domain.model.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.panini.tickets.data.repository.TicketRepository
import kotlinx.coroutines.launch

data class TicketsUiState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val error: String? = null
)

class TicketsViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsUiState())
    val uiState: StateFlow<TicketsUiState> = _uiState.asStateFlow()

    init {
        fetchTickets()
        observeEvents()
    }

    fun fetchTickets() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = repository.getTickets()) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        tickets = result.data.sortedByPriority(),
                        error = null
                    )
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            TicketEventBus.events.collect {
                fetchTickets()
            }
        }
    }

    private fun List<Ticket>.sortedByPriority(): List<Ticket> =
        sortedWith(
            compareByDescending<Ticket> { it.priority.weight }
                .thenByDescending { it.createdAt }
        )
}

class TicketsViewModelFactory(
    private val repository: TicketRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TicketsViewModel(repository) as T
}
