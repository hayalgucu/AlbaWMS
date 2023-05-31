package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemLocationModel(
    @SerialName("AlinancakMiktar")
    var alinancakMiktar: Double,
    @SerialName("altkonum")
    val altkonum: String,
    @SerialName("DepoMiktar")
    val depoMiktar: Double,
    @SerialName("hcr_konum_no")
    val hcrKonumNo: Int,
    @SerialName("hcr_makine_no")
    val hcrMakineNo: Int,
    @SerialName("konumdanaldim")
    var konumdanaldim: Boolean,
    @SerialName("sip_kalan")
    val sipKalan: Double,
    @SerialName("sip_recno")
    val sipRecno: Int,
    @SerialName("sip_stok_kodu")
    var sipStokKodu: String,
    @SerialName("sip_teslim_miktar")
    val sipTeslimMiktar: Double,
    @SerialName("stk_adi")
    val stkAdi: String
)