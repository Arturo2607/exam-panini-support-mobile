package com.panini.tickets.core.event

sealed class TicketEvent {
    data class Created(val ticketId: Int) : TicketEvent()
    data class StatusChanged(val ticketId: Int) : TicketEvent()
    data class PriorityChanged(val ticketId: Int) : TicketEvent()
}
