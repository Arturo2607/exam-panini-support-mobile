package com.panini.tickets.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateTicketStatusRequest(
    @SerializedName("status")
    val status: String
)
