package com.hayalgucu.albawms.models

import kotlinx.serialization.SerialName

data class ProgramUpdateModel(
    @SerialName("ftp_pass")
    val ftpPass: String,
    @SerialName("ftp_server")
    val ftpServer: String,
    @SerialName("ftp_user")
    val ftpUser: String,
    @SerialName("id")
    val id: Int,
    @SerialName("version")
    val version: String
)