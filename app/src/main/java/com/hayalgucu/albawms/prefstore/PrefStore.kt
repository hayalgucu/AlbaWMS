package com.hayalgucu.albawms.prefstore

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    fun getDeviceName(): Flow<String>
    suspend fun saveDeviceName(value: String)

    fun getDeviceId(): Flow<String>
    suspend fun saveDeviceId(deviceId: String)

    fun getDeviceModel(): Flow<String>
    suspend fun saveDeviceModel(deviceModel: String)

    fun getPrinterName(): Flow<String>
    suspend fun savePrinterName(value: String)

    fun getUserId(): Flow<String>
    suspend fun saveUser(userId: Int)

    fun getServerTimeDiff(): Flow<String>
    suspend fun saveServerTimeDiff(dateTime: String)

    fun getFirmName(): Flow<String>
    suspend fun saveFirmName(firmName: String)

    fun getIsImageShown(): Flow<Boolean>
    suspend fun saveIsImageShown(value: Boolean)

    fun getServerAddress(): Flow<String>
    suspend fun saveServerAddress(address: String)

    fun getKeyboardOptions(): Flow<Boolean>
    suspend fun saveKeyboardOptions(value: Boolean)

    fun getInactivity(): Flow<Long>
    suspend fun saveInactivity(value: Long)

    fun getMACAddress(): Flow<String>
    suspend fun saveMACAddress(value: String)

    fun getEnterWarehouse(): Flow<Int>
    suspend fun saveEnterWarehouse(value: Int)

    fun getExitWarehouse(): Flow<Int>
    suspend fun saveExitWarehouse(value: Int)

    fun getStartDate(): Flow<Long>
    suspend fun saveStartDate(value: Long)

    fun getUsername(): Flow<String>
    suspend fun saveUsername(value: String)

    fun getTerminator(): Flow<String>
    suspend fun saveTerminator(value: String)

    fun getUseNameInCount(): Flow<Boolean>
    suspend fun saveUseNameInCount(value: Boolean)

    companion object {
        fun create(context: Context) = PrefStoreImpl(context)
    }
}