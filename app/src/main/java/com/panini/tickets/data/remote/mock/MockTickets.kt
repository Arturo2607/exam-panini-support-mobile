package com.panini.tickets.data.remote.mock

import com.panini.tickets.data.remote.dto.TicketResponse

object MockTickets {

    fun seed(): List<TicketResponse> = listOf(
        TicketResponse(
            ticketId    = 1,
            title       = "Faltante de sobres en bodega central",
            description = "La bodega central reporta 4.500 sobres menos respecto al despacho confirmado por el proveedor para la edición estándar del álbum.",
            priority    = "CRITICAL",
            status      = "OPEN",
            supplier    = "Bodega Central San José",
            category    = "INVENTORY",
            createdAt   = "2026-05-29",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 2,
            title       = "Retraso en entrega de álbumes a punto de venta Heredia",
            description = "El transportista no entregó el lote programado para los puntos de venta de Heredia; clientes sin stock desde hace dos días.",
            priority    = "HIGH",
            status      = "IN_PROGRESS",
            supplier    = "Transportes LogiCR",
            category    = "DISTRIBUTION",
            createdAt   = "2026-05-28",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 3,
            title       = "Cromos duplicados en lote 2026-A",
            description = "El lote 2026-A llegó con repetición excesiva de cromos del set de selecciones, fuera del rango de aleatoriedad acordado.",
            priority    = "MEDIUM",
            status      = "OPEN",
            supplier    = "Imprenta Panini MX",
            category    = "PACKAGING",
            createdAt   = "2026-05-27",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 4,
            title       = "Proveedor no confirma despacho de stickers edición especial",
            description = "El proveedor internacional no confirma fecha de despacho de los stickers de la edición especial, bloqueando la preventa.",
            priority    = "HIGH",
            status      = "OPEN",
            supplier    = "Panini Group Italia",
            category    = "SUPPLIER",
            createdAt   = "2026-05-26",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 5,
            title       = "Inventario descuadrado entre sistema y conteo físico",
            description = "El centro de distribución de Alajuela presenta diferencias entre el inventario del sistema y el conteo físico de paquetes.",
            priority    = "MEDIUM",
            status      = "IN_PROGRESS",
            supplier    = "Centro de Distribución Alajuela",
            category    = "INVENTORY",
            createdAt   = "2026-05-25",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 6,
            title       = "Paquetes dañados durante el transporte",
            description = "Se recibieron cajas con sobres húmedos y dañados por mala manipulación durante el traslado interurbano.",
            priority    = "LOW",
            status      = "RESOLVED",
            supplier    = "Courier Express",
            category    = "LOGISTICS",
            createdAt   = "2026-05-24",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 7,
            title       = "Punto de venta Cartago sin reposición de álbumes",
            description = "El punto de venta de Cartago agotó existencias y no recibe reposición programada pese a la alta demanda.",
            priority    = "CRITICAL",
            status      = "OPEN",
            supplier    = "Librería Universal Cartago",
            category    = "DISTRIBUTION",
            createdAt   = "2026-05-23",
            createdBy   = 1
        ),
        TicketResponse(
            ticketId    = 8,
            title       = "Error de etiquetado en cajas de exportación",
            description = "Las cajas destinadas a distribución regional llegaron con etiquetas de destino incorrectas, generando reenvíos.",
            priority    = "MEDIUM",
            status      = "CLOSED",
            supplier    = "Imprenta Panini MX",
            category    = "PACKAGING",
            createdAt   = "2026-05-22",
            createdBy   = 1
        )
    )
}
