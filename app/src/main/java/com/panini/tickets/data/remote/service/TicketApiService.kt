package com.panini.tickets.data.remote.service

import com.panini.tickets.core.AppConstants
import com.panini.tickets.data.remote.dto.CreateTicketRequest
import com.panini.tickets.data.remote.dto.TicketResponse
import com.panini.tickets.data.remote.dto.UpdateTicketPriorityRequest
import com.panini.tickets.data.remote.dto.UpdateTicketStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketApiService {

    @GET(AppConstants.Api.Paths.TICKETS)
    suspend fun getTickets(): Response<List<TicketResponse>>

    @GET(AppConstants.Api.Paths.TICKET_BY_ID)
    suspend fun getTicketById(
        @Path("ticketId") ticketId: Int
    ): Response<TicketResponse>

    @POST(AppConstants.Api.Paths.TICKETS)
    suspend fun createTicket(
        @Body request: CreateTicketRequest
    ): Response<TicketResponse>

    @PATCH(AppConstants.Api.Paths.TICKET_STATUS)
    suspend fun updateTicketStatus(
        @Path("ticketId") ticketId: Int,
        @Body request: UpdateTicketStatusRequest
    ): Response<TicketResponse>

    @PATCH(AppConstants.Api.Paths.TICKET_PRIORITY)
    suspend fun updateTicketPriority(
        @Path("ticketId") ticketId: Int,
        @Body request: UpdateTicketPriorityRequest
    ): Response<TicketResponse>
}
