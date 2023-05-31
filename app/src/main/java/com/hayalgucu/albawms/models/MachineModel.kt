package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MachineModel(
    @SerialName("color")
    val color: String?,
    @SerialName("DeviceType")
    val deviceType: Int,
    @SerialName("H")
    val h: Int,
    @SerialName("lazerCount")
    val lazerCount: Int,
    @SerialName("LocationCode")
    val locationCode: String,
    @SerialName("name")
    val name: String,
    @SerialName("no")
    val no: Int,
    @SerialName("SCR")
    val sCR: Int,
    @SerialName("TrayCount")
    val trayCount: Int,
    @SerialName("W")
    val w: Int
)