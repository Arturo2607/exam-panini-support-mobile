package com.panini.tickets.ui.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panini.tickets.core.UserMessages
import com.panini.tickets.di.AppContainer
import com.panini.tickets.domain.model.TicketCategory
import com.panini.tickets.domain.model.TicketPriority
import com.panini.tickets.ui.components.label
import com.panini.tickets.ui.theme.AppSecondaryText

@Composable
fun CreateTicketScreen(
    createdBy: Int,
    snackbarHostState: SnackbarHostState,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateTicketViewModel = viewModel(
        factory = CreateTicketViewModelFactory(AppContainer.ticketRepository, createdBy)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.created.collect { message ->
            snackbarHostState.showSnackbar(message)
            onSaved()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = UserMessages.TicketCreate.INTRO,
            style = MaterialTheme.typography.bodyMedium,
            color = AppSecondaryText
        )
        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text(UserMessages.TicketCreate.TITLE_LABEL) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text(UserMessages.TicketCreate.DESCRIPTION_LABEL) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.supplier,
            onValueChange = viewModel::onSupplierChange,
            label = { Text(UserMessages.TicketCreate.SUPPLIER_LABEL) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        EnumDropdown(
            label = UserMessages.TicketCreate.PRIORITY_LABEL,
            options = TicketPriority.entries,
            selected = uiState.priority,
            labelOf = { it.label() },
            onSelect = viewModel::onPriorityChange
        )
        EnumDropdown(
            label = UserMessages.TicketCreate.CATEGORY_LABEL,
            options = TicketCategory.entries,
            selected = uiState.category,
            labelOf = { it.label() },
            onSelect = viewModel::onCategoryChange
        )
        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Button(
            onClick = viewModel::submit,
            enabled = !uiState.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(UserMessages.TicketCreate.SUBMIT)
            }
        }
    }
}

@Composable
private fun <T> EnumDropdown(
    label: String,
    options: List<T>,
    selected: T,
    labelOf: (T) -> String,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = labelOf(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp
                    else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(labelOf(option)) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}