package com.panini.tickets.domain.model

enum class TicketCategory {
    SUPPLIER,
    INVENTORY,
    DISTRIBUTION,
    LOGISTICS,
    PACKAGING,
    OTHER;

    companion object {
        fun from(raw: String?): TicketCategory =
            entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: OTHER
    }
}
