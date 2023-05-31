package com.hayalgucu.albawms.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemModel(
    @SerialName("birimadi")
    val birimadi: String,
    @SerialName("DepoMiktari")
    val depoMiktari: Double,
    @SerialName("konum")
    val konum: String?,
    @SerialName("konum_no")
    val konumNo: Int,
    @SerialName("makina_no")
    val makinaNo: Int,
    @SerialName("stk_aciklama")
    val stkAciklama: String,
    @SerialName("stk_adi")
    val stkAdi: String,
    @SerialName("stk_birim")
    val stkBirim: String?,
    @SerialName("stk_birim_pnt")
    val stkBirimPnt: Int,
    @SerialName("stk_create_date")
    val stkCreateDate: String,
    @SerialName("stk_create_user")
    val stkCreateUser: Int,
    @SerialName("stk_grup_adi")
    val stkGrupAdi: String,
    @SerialName("stk_grup_kodu")
    val stkGrupKodu: String,
    @SerialName("stk_image")
    val stkImage: String?,
    @SerialName("stk_img_filename")
    val stkImgFilename: String,
    @SerialName("stk_kodu")
    val stkKodu: String,
    @SerialName("stk_kritik_seviye")
    val stkKritikSeviye: Double,
    @SerialName("stk_lastup_date")
    val stkLastupDate: String,
    @SerialName("stk_lastup_user")
    val stkLastupUser: Int,
    @SerialName("stk_max_seviye")
    val stkMaxSeviye: Double,
    @SerialName("stk_pasif_fl")
    val stkPasifFl: Boolean,
    @SerialName("stk_recno")
    val stkRecno: Int,
    @SerialName("stk_var_konum")
    val stkVarKonum: String
)