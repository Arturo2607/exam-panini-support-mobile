package com.panini.tickets.navigation

object AppDestinations {

    const val ARG_TICKET_ID = "ticketId"

    const val LOGIN          = "login"
    const val TICKETS        = "tickets"
    const val TICKET_CREATE  = "ticket_create"
    const val TICKET_DETAIL  = "ticket_detail/{$ARG_TICKET_ID}"

    fun ticketDetail(ticketId: Int): String = "ticket_detail/$ticketId"
}
