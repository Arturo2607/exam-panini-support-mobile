package com.panini.tickets.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.tickets.data.repository.TicketRepository
import com.panini.tickets.core.UserMessages
import com.panini.tickets.core.event.TicketEvent
import com.panini.tickets.core.event.TicketEventBus
import com.panini.tickets.data.remote.ApiResult
import com.panini.tickets.data.remote.dto.CreateTicketRequest
import com.panini.tickets.domain.model.TicketCategory
import com.panini.tickets.domain.model.TicketPriority
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateTicketUiState(
    val title: String = "",
    val description: String = "",
    val supplier: String = "",
    val priority: TicketPriority = TicketPriority.MEDIUM,
    val category: TicketCategory = TicketCategory.SUPPLIER,
    val isSubmitting: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank() && supplier.isNotBlank()
}

class CreateTicketViewModel(
    private val repository: TicketRepository,
    private val createdBy: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    private val _created = MutableSharedFlow<String>()
    val created: SharedFlow<String> = _created.asSharedFlow()

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, error = null) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value, error = null) }
    fun onSupplierChange(value: String) = _uiState.update { it.copy(supplier = value, error = null) }
    fun onPriorityChange(value: TicketPriority) = _uiState.update { it.copy(priority = value) }
    fun onCategoryChange(value: TicketCategory) = _uiState.update { it.copy(category = value) }

    fun submit() {
        val state = _uiState.value
        if (!state.isValid || state.isSubmitting) {
            _uiState.update { it.copy(error = UserMessages.TicketCreate.VALIDATION_ERROR) }
            return
        }
        _uiState.update { it.copy(isSubmitting = true, error = null) }
        viewModelScope.launch {
            val request = CreateTicketRequest(
                title       = state.title.trim(),
                description = state.description.trim(),
                priority    = state.priority.name,
                supplier    = state.supplier.trim(),
                category    = state.category.name,
                createdBy   = createdBy
            )
            when (val result = repository.createTicket(request)) {
                is ApiResult.Success -> {
                    TicketEventBus.emit(TicketEvent.Created(result.data.ticketId))
                    _created.emit(UserMessages.Tickets.CREATE_SUCCESS)
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, error = result.message)
                }
            }
        }
    }
}

class CreateTicketViewModelFactory(
    private val repository: TicketRepository,
    private val createdBy: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CreateTicketViewModel(repository, createdBy) as T
}
