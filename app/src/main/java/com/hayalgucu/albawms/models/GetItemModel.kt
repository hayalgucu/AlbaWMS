package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetItemModel(
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("stockcode")
    val itemCode: String
)