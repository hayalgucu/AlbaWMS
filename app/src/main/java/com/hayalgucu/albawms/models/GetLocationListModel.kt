package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLocationInfoModel(
    @SerialName("machineNumber")
    val machineno: Int,
    @SerialName("shelfno")
    val shelfno: Int
)