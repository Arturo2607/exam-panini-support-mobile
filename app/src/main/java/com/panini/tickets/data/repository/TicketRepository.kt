package com.panini.tickets.data.repository

import com.panini.tickets.data.remote.ApiErrorHandler
import com.panini.tickets.data.remote.ApiResult
import com.panini.tickets.data.remote.dto.CreateTicketRequest
import com.panini.tickets.data.remote.dto.UpdateTicketPriorityRequest
import com.panini.tickets.data.remote.dto.UpdateTicketStatusRequest
import com.panini.tickets.data.remote.dto.toDomain
import com.panini.tickets.data.remote.service.TicketApiService
import com.panini.tickets.domain.model.Ticket

class TicketRepository(
    private val apiService: TicketApiService
) {

    suspend fun getTickets(): ApiResult<List<Ticket>> {
        return try {
            val response = apiService.getTickets()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()?.map { it.toDomain() }.orEmpty())
            } else {
                ApiResult.Error(
                    message = ApiErrorHandler.fromHttpCode(response.code()),
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiErrorHandler.fromException(e))
        }
    }

    suspend fun getTicketById(ticketId: Int): ApiResult<Ticket> {
        return try {
            val response = apiService.getTicketById(ticketId)
            if (response.isSuccessful) {
                val ticket = response.body()?.toDomain()
                    ?: return ApiResult.Error("Ticket no encontrado")
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(
                    message = ApiErrorHandler.fromHttpCode(response.code()),
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiErrorHandler.fromException(e))
        }
    }

    suspend fun createTicket(request: CreateTicketRequest): ApiResult<Ticket> {
        return try {
            val response = apiService.createTicket(request)
            if (response.isSuccessful) {
                val ticket = response.body()?.toDomain()
                    ?: return ApiResult.Error("Error al crear el ticket")
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(
                    message = ApiErrorHandler.fromHttpCode(response.code()),
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiErrorHandler.fromException(e))
        }
    }

    suspend fun updateStatus(ticketId: Int, status: String): ApiResult<Ticket> {
        return try {
            val response = apiService.updateTicketStatus(ticketId, UpdateTicketStatusRequest(status))
            if (response.isSuccessful) {
                val ticket = response.body()?.toDomain()
                    ?: return ApiResult.Error("Respuesta vacía del servidor")
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(
                    message = ApiErrorHandler.fromHttpCode(response.code()),
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiErrorHandler.fromException(e))
        }
    }

    suspend fun updatePriority(ticketId: Int, priority: String): ApiResult<Ticket> {
        return try {
            val response = apiService.updateTicketPriority(ticketId, UpdateTicketPriorityRequest(priority))
            if (response.isSuccessful) {
                val ticket = response.body()?.toDomain()
                    ?: return ApiResult.Error("Respuesta vacía del servidor")
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(
                    message = ApiErrorHandler.fromHttpCode(response.code()),
                    statusCode = response.code()
                )
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiErrorHandler.fromException(e))
        }
    }
}
