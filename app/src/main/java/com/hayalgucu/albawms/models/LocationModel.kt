package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationModel(
    @SerialName("degisti")
    val degisti: Boolean,
    @SerialName("DepoMiktari")
    val depoMiktari: Double,
    @SerialName("hca_create_date")
    val hcaCreateDate: String,
    @SerialName("hca_create_user")
    val hcaCreateUser: Int,
    @SerialName("hca_konum")
    val hcaKonum: String,
    @SerialName("hca_konum_recno")
    val hcaKonumRecno: Int,
    @SerialName("hca_kutu_recno")
    val hcaKutuRecno: Int,
    @SerialName("hca_lastup_date")
    val hcaLastupDate: String,
    @SerialName("hca_lastup_user")
    val hcaLastupUser: Int,
    @SerialName("hca_lazer_X")
    val hcaLazerX: Int,
    @SerialName("hca_lazer_Y")
    val hcaLazerY: Int,
    @SerialName("hca_part")
    val hcaPart: String,
    @SerialName("hca_pasif")
    val hcaPasif: Boolean,
    @SerialName("hca_recno")
    val hcaRecno: Int,
    @SerialName("hca_sirano")
    val hcaSirano: Int,
    @SerialName("hcr_konum_no")
    val hcrKonumNo: Int,
    @SerialName("hcr_makine_no")
    val hcrMakineNo: Int,
    @SerialName("KonumDolu")
    val konumDolu: Boolean,
    @SerialName("ktp_kutu")
    val ktpKutu: String?,
    @SerialName("Location")
    val location: String?,
    @SerialName("stok_ismi")
    val stokIsmi: String?,
    @SerialName("stok_kodu")
    val stokKodu: String?
)