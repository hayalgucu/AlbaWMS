package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    @SerialName("usr_admin")
    val usrAdmin: Boolean,
    @SerialName("usr_code")
    val usrCode: String,
    @SerialName("usr_create_date")
    val usrCreateDate: String,
    @SerialName("usr_create_user")
    val usrCreateUser: Int,
    @SerialName("usr_firstname")
    val usrFirstname: String,
    @SerialName("usr_fullname")
    val usrFullname: String,
    @SerialName("usr_lastname")
    val usrLastname: String,
    @SerialName("usr_lastup_date")
    val usrLastupDate: String,
    @SerialName("usr_lastup_user")
    val usrLastupUser: Int,
    @SerialName("usr_passive")
    val usrPassive: Boolean,
    @SerialName("usr_password")
    val usrPassword: String,
    @SerialName("usr_recno")
    val usrRecno: Int,
    @SerialName("usr_use_machine")
    val usrUseMachine: Boolean
)