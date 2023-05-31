package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShelfModel(
    @SerialName("altkonum")
    val altkonum: String,
    @SerialName("_altkonum")
    val subLocations: SubLocation,
    @SerialName("hcr_create_date")
    val hcrCreateDate: String,
    @SerialName("hcr_create_user")
    val hcrCreateUser: Int,
    @SerialName("hcr_konum_no")
    val hcrKonumNo: Int,
    @SerialName("hcr_kul_kisitli")
    val hcrKulKisitli: Boolean,
    @SerialName("hcr_lastup_date")
    val hcrLastupDate: String,
    @SerialName("hcr_lastup_user")
    val hcrLastupUser: Int,
    @SerialName("hcr_makine_no")
    val hcrMakineNo: Int,
    @SerialName("hcr_recno")
    val hcrRecno: Int
)