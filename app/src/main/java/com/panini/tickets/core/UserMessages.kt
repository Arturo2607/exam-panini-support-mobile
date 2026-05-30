package com.panini.tickets.core

object UserMessages {

    const val TAP_TO_RETRY = "Toca para reintentar."

    object Login {
        const val TITLE               = "Iniciar sesión"
        const val INTRO               = "Accede para gestionar los tickets de soporte."
        const val EMAIL_LABEL         = "Correo electrónico"
        const val PASSWORD_LABEL      = "Contraseña"
        const val SUBMIT              = "Ingresar"
        const val INVALID_CREDENTIALS = "Credenciales inválidas"
        const val SUCCESS             = "Sesión iniciada correctamente"
    }

    object Tickets {
        const val TITLE             = "Tickets de soporte"
        const val LOADING           = "Cargando tickets..."
        const val EMPTY             = "No hay tickets registrados"
        const val SUPPLIER_PREFIX   = "Proveedor: "
        const val CREATED_AT_PREFIX = "Creado: "
        const val CREATE_SUCCESS    = "Ticket creado correctamente"
        const val CREATE_ERROR      = "No se pudo crear el ticket"
    }

    object TicketDetail {
        const val TITLE                   = "Detalle del ticket"
        const val DESCRIPTION_LABEL       = "Descripción"
        const val SUPPLIER_LABEL          = "Proveedor"
        const val CATEGORY_LABEL          = "Categoría"
        const val PRIORITY_LABEL          = "Prioridad"
        const val STATUS_LABEL            = "Estado"
        const val CREATED_AT_LABEL        = "Fecha de creación"
        const val CHANGE_STATUS           = "Cambiar estado"
        const val CHANGE_PRIORITY         = "Cambiar prioridad"
        const val STATUS_UPDATE_SUCCESS   = "Estado actualizado"
        const val STATUS_UPDATE_ERROR     = "No se pudo actualizar el estado"
        const val PRIORITY_UPDATE_SUCCESS = "Prioridad actualizada"
        const val PRIORITY_UPDATE_ERROR   = "No se pudo actualizar la prioridad"
    }

    object TicketCreate {
        const val TITLE             = "Nuevo ticket"
        const val INTRO             = "Completa la información del ticket de soporte."
        const val TITLE_LABEL       = "Título"
        const val DESCRIPTION_LABEL = "Descripción"
        const val SUPPLIER_LABEL    = "Proveedor"
        const val PRIORITY_LABEL    = "Prioridad"
        const val CATEGORY_LABEL    = "Categoría"
        const val SUBMIT            = "Crear ticket"
        const val VALIDATION_ERROR  = "Completa los campos obligatorios"
    }

    object Priority {
        const val CRITICAL = "Crítica"
        const val HIGH     = "Alta"
        const val MEDIUM   = "Media"
        const val LOW      = "Baja"
    }

    object Status {
        const val OPEN        = "Abierto"
        const val IN_PROGRESS = "En progreso"
        const val RESOLVED    = "Resuelto"
        const val CLOSED      = "Cerrado"
    }

    object Category {
        const val SUPPLIER     = "Proveedor"
        const val INVENTORY    = "Inventario"
        const val DISTRIBUTION = "Distribución"
        const val LOGISTICS    = "Logística"
        const val PACKAGING    = "Empaque"
        const val OTHER        = "Otro"
    }

    object Accessibility {
        const val BACK   = "Volver"
        const val LOGOUT = "Cerrar sesión"
        const val ADD    = "Agregar"
    }

    object Common {
        const val SAVE   = "Guardar"
        const val CANCEL = "Cancelar"
    }
}
