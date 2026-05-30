package com.panini.tickets.data.remote.service

import com.panini.tickets.data.remote.dto.CreateTicketRequest
import com.panini.tickets.data.remote.dto.TicketResponse
import com.panini.tickets.data.remote.dto.UpdateTicketPriorityRequest
import com.panini.tickets.data.remote.dto.UpdateTicketStatusRequest
import com.panini.tickets.data.remote.mock.MockTickets
import kotlinx.coroutines.delay
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FakeTicketApiService : TicketApiService {

    private val tickets = MockTickets.seed().toMutableList()

    override suspend fun getTickets(): Response<List<TicketResponse>> {
        delay(NETWORK_DELAY_MS)
        return Response.success(tickets.toList())
    }

    override suspend fun getTicketById(ticketId: Int): Response<TicketResponse> {
        delay(NETWORK_DELAY_MS)
        val ticket = tickets.firstOrNull { it.ticketId == ticketId }
            ?: return notFound()
        return Response.success(ticket)
    }

    override suspend fun createTicket(request: CreateTicketRequest): Response<TicketResponse> {
        delay(NETWORK_DELAY_MS)
        val nextId = (tickets.maxOfOrNull { it.ticketId } ?: 0) + 1
        val created = TicketResponse(
            ticketId    = nextId,
            title       = request.title,
            description = request.description,
            priority    = request.priority,
            status      = "OPEN",
            supplier    = request.supplier,
            category    = request.category,
            createdAt   = currentDate(),
            createdBy   = request.createdBy
        )
        tickets.add(0, created)
        return Response.success(created)
    }

    override suspend fun updateTicketStatus(
        ticketId: Int,
        request: UpdateTicketStatusRequest
    ): Response<TicketResponse> {
        delay(NETWORK_DELAY_MS)
        val index = tickets.indexOfFirst { it.ticketId == ticketId }
        if (index == -1) return notFound()
        val updated = tickets[index].copy(status = request.status)
        tickets[index] = updated
        return Response.success(updated)
    }

    override suspend fun updateTicketPriority(
        ticketId: Int,
        request: UpdateTicketPriorityRequest
    ): Response<TicketResponse> {
        delay(NETWORK_DELAY_MS)
        val index = tickets.indexOfFirst { it.ticketId == ticketId }
        if (index == -1) return notFound()
        val updated = tickets[index].copy(priority = request.priority)
        tickets[index] = updated
        return Response.success(updated)
    }

    private fun <T> notFound(): Response<T> =
        Response.error(404, "".toResponseBody(null))

    private fun currentDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    companion object {
        private const val NETWORK_DELAY_MS = 400L
    }
}
