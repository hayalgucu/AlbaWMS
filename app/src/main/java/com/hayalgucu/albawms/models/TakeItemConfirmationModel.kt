package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TakeItemConfirmationModel(
    @SerialName("location")
    val location: String,
    @SerialName("machineno")
    val machineno: Int,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("shelfno")
    val shelfno: Int,
    @SerialName("stockcode")
    val stockcode: String,
    @SerialName("userno")
    val userNo: Int
)