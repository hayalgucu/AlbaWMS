package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShelfsLocation(
    @SerialName("c")
    val c: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("r")
    val r: Int,
    @SerialName("X")
    val x: Int,
    @SerialName("Y")
    val y: Int
)