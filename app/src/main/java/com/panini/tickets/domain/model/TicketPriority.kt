package com.panini.tickets.domain.model

enum class TicketPriority(val weight: Int) {
    CRITICAL(4),
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    companion object {
        fun from(raw: String?): TicketPriority =
            entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: MEDIUM
    }
}
