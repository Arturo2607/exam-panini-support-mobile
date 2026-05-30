package com.panini.tickets.di

import com.panini.tickets.data.remote.service.FakeTicketApiService
import com.panini.tickets.data.remote.service.TicketApiService
import com.panini.tickets.data.repository.TicketRepository

object AppContainer {

    private val ticketApiService: TicketApiService by lazy {
        FakeTicketApiService()
    }

    val ticketRepository: TicketRepository by lazy {
        TicketRepository(ticketApiService)
    }
}