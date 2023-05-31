package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLocationListModel(
    @SerialName("machineNumber")
    val machineno: Int,
    @SerialName("shelfno")
    val shelfno: Int
)