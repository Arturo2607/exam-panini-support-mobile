package com.panini.tickets.core

object AppConstants {

    object Api {

        const val BASE_URL = "https://api.paninisupport.local/"

        object Paths {
            const val TICKETS         = "api/tickets"
            const val TICKET_BY_ID    = "api/tickets/{ticketId}"
            const val TICKET_STATUS   = "api/tickets/{ticketId}/status"
            const val TICKET_PRIORITY = "api/tickets/{ticketId}/priority"
        }
    }
}
