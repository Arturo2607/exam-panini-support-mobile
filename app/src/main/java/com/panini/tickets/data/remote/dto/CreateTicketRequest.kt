package com.panini.tickets.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateTicketRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("priority")
    val priority: String,

    @SerializedName("supplier")
    val supplier: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("createdBy")
    val createdBy: Int
)
