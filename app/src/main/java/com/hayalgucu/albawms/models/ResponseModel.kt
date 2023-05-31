package com.hayalgucu.albawms.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel<T>(
    @SerialName("Data")
    val data: T?,
    @SerialName("StatusCode")
    var statusCode: Int,
    @SerialName("Error")
    val error: ErrorModel?,
    @SerialName("IsSuccessful")
    val isSuccessful: Boolean
)

@Serializable
data class ErrorModel(
    @SerialName("Errors")
    var errors: List<String>
)