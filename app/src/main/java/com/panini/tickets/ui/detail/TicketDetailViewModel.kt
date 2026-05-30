package com.panini.tickets.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.tickets.data.repository.TicketRepository
import com.panini.tickets.core.UserMessages
import com.panini.tickets.core.event.TicketEvent
import com.panini.tickets.core.event.TicketEventBus
import com.panini.tickets.data.remote.ApiResult
import com.panini.tickets.domain.model.Ticket
import com.panini.tickets.domain.model.TicketPriority
import com.panini.tickets.domain.model.TicketStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val ticket: Ticket? = null,
    val error: String? = null,
    val isUpdating: Boolean = false
)

class TicketDetailViewModel(
    private val repository: TicketRepository,
    private val ticketId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState: StateFlow<TicketDetailUiState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = repository.getTicketById(ticketId)) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(isLoading = false, ticket = result.data, error = null)
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun updateStatus(status: TicketStatus) {
        if (_uiState.value.isUpdating) return
        _uiState.update { it.copy(isUpdating = true) }
        viewModelScope.launch {
            when (val result = repository.updateStatus(ticketId, status.name)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isUpdating = false, ticket = result.data) }
                    TicketEventBus.emit(TicketEvent.StatusChanged(ticketId))
                    _messages.emit(UserMessages.TicketDetail.STATUS_UPDATE_SUCCESS)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isUpdating = false) }
                    _messages.emit(UserMessages.TicketDetail.STATUS_UPDATE_ERROR)
                }
            }
        }
    }

    fun updatePriority(priority: TicketPriority) {
        if (_uiState.value.isUpdating) return
        _uiState.update { it.copy(isUpdating = true) }
        viewModelScope.launch {
            when (val result = repository.updatePriority(ticketId, priority.name)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isUpdating = false, ticket = result.data) }
                    TicketEventBus.emit(TicketEvent.PriorityChanged(ticketId))
                    _messages.emit(UserMessages.TicketDetail.PRIORITY_UPDATE_SUCCESS)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isUpdating = false) }
                    _messages.emit(UserMessages.TicketDetail.PRIORITY_UPDATE_ERROR)
                }
            }
        }
    }
}

class TicketDetailViewModelFactory(
    private val repository: TicketRepository,
    private val ticketId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TicketDetailViewModel(repository, ticketId) as T
}
