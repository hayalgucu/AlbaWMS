package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemsInLocationModel(
    @SerialName("kst_konum")
    val kstKonum: String,
    @SerialName("miktar")
    val miktar: Double,
    @SerialName("stk_adi")
    val stkAdi: String,
    @SerialName("stk_kodu")
    val stkKodu: String
)