package com.panini.tickets.domain.model

data class Ticket(
    val ticketId: Int,
    val title: String,
    val description: String,
    val priority: TicketPriority,
    val status: TicketStatus,
    val supplier: String,
    val category: TicketCategory,
    val createdAt: String,
    val createdBy: Int
)
