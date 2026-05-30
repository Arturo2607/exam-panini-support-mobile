package com.panini.tickets.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.panini.tickets.domain.model.Ticket
import com.panini.tickets.domain.model.TicketCategory
import com.panini.tickets.domain.model.TicketPriority
import com.panini.tickets.domain.model.TicketStatus

data class TicketResponse(
    @SerializedName("ticketId")
    val ticketId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("priority")
    val priority: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("supplier")
    val supplier: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("createdBy")
    val createdBy: Int
)

fun TicketResponse.toDomain(): Ticket = Ticket(
    ticketId    = ticketId,
    title       = title,
    description = description,
    priority    = TicketPriority.from(priority),
    status      = TicketStatus.from(status),
    supplier    = supplier,
    category    = TicketCategory.from(category),
    createdAt   = createdAt,
    createdBy   = createdBy
)
