package com.panini.tickets.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateTicketPriorityRequest(
    @SerializedName("priority")
    val priority: String
)
