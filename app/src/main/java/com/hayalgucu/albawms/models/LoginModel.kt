package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginModel(
    @SerialName("userName")
    val userName: String,
    @SerialName("password")
    val password: String,
    @SerialName("persistAccount")
    val persistAccount: Boolean = true
)