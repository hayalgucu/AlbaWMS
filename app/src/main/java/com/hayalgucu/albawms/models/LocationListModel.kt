package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationListModel(
    @SerialName("location")
    val location: String,
    @SerialName("machineNumber")
    val machineNumber: Int,
    @SerialName("shelfNo")
    val shelfNo: Int
)