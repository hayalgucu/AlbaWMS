package com.hayalgucu.albawms.prefstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.hayalgucu.albawms.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefStoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : PrefsStore {
    private val dataStore: DataStore<Preferences> = context.dataStore

    override fun getDeviceName(): Flow<String> {
        val name = dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[DEVICE_NAME] ?: "" }
        return name
    }

    override suspend fun saveDeviceName(value: String) {
        dataStore.edit {
            it[DEVICE_NAME] = value
        }
    }

    override fun getDeviceId(): Flow<String> {
        val id = dataStore.data.catch { exception -> // 1
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) { // 2
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[BLUETOOTH_DEVICE] ?: "" }
        return id
    }

    override suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit {
            it[BLUETOOTH_DEVICE] = deviceId
        }
    }

    override fun getPrinterName(): Flow<String> {
        val name = dataStore.data.catch { exception -> // 1
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) { // 2
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[PRINTER_NAME] ?: "" }
        return name
    }

    override suspend fun savePrinterName(value: String) {
        dataStore.edit {
            it[PRINTER_NAME] = value
        }
    }

    override fun getDeviceModel(): Flow<String> {
        val model = dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[DEVICE_MODEL] ?: "" }
        return model
    }

    override suspend fun saveDeviceModel(deviceModel: String) {
        dataStore.edit {
            it[DEVICE_MODEL] = deviceModel
        }
    }

    override fun getUserId(): Flow<String> {
        val userId = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[USER_ID] ?: "" }

        return userId
    }

    override suspend fun saveUser(userId: Int) {
        dataStore.edit {
            it[USER_ID] = userId.toString()
        }
    }

    override fun getServerTimeDiff(): Flow<String> {
        val serverTime = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[SERVER_TIME] ?: "" }

        return serverTime
    }

    override suspend fun saveServerTimeDiff(dateTime: String) {
        val serverTime = LocalDateTime.parse(dateTime, apiDateTimeFormatter).toEpochSecond(
            ZoneOffset.UTC
        )
        val now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        dataStore.edit {
            it[SERVER_TIME] = (serverTime - now).toString()
        }
    }

    override fun getFirmName(): Flow<String> {
        val firmName = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[FIRM_NAME] ?: "" }
        return firmName
    }

    override suspend fun saveFirmName(firmName: String) {
        dataStore.edit {
            it[FIRM_NAME] = firmName
        }
    }

    override fun getIsImageShown(): Flow<Boolean> {
        val value = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[IS_IMAGE_SHOWN] ?: false }
        return value
    }

    override suspend fun saveIsImageShown(value: Boolean) {
        dataStore.edit {
            it[IS_IMAGE_SHOWN] = value
        }
    }

    override fun getServerAddress(): Flow<String> {
        val baseUrl = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[SERVER_ADDRESS] ?: "" }
        return baseUrl
    }

    override suspend fun saveServerAddress(address: String) {
        dataStore.edit {
            it[SERVER_ADDRESS] = address
        }
    }

    override fun getKeyboardOptions(): Flow<Boolean> {
        val keyboardOptions = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[KEYBOARD_OPTIONS] ?: false }
        return keyboardOptions
    }

    override suspend fun saveKeyboardOptions(value: Boolean) {
        dataStore.edit {
            it[KEYBOARD_OPTIONS] = value
        }
    }

    override fun getInactivity(): Flow<Long> {
        val inactivity = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[INACTIVITY_TIME] ?: inactivityTime.value }
        return inactivity
    }

    override suspend fun saveInactivity(value: Long) {
        dataStore.edit {
            it[INACTIVITY_TIME] = value
        }
    }

    override fun getMACAddress(): Flow<String> {
        val address = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[MAC_ADDRESS] ?: "" }
        return address
    }

    override suspend fun saveMACAddress(value: String) {
        dataStore.edit {
            it[MAC_ADDRESS] = value
        }
    }

    override fun getEnterWarehouse(): Flow<Int> {
        val warehouse = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[ENTER_WAREHOUSE] ?: 3 }
        return warehouse
    }

    override suspend fun saveEnterWarehouse(value: Int) {
        dataStore.edit {
            it[ENTER_WAREHOUSE] = value
        }
    }

    override fun getExitWarehouse(): Flow<Int> {
        val warehouse = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[EXIT_WAREHOUSE] ?: 1 }
        return warehouse
    }

    override suspend fun saveExitWarehouse(value: Int) {
        dataStore.edit {
            it[EXIT_WAREHOUSE] = value
        }
    }

    override fun getStartDate(): Flow<Long> {
        val startDate = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[START_DATE] ?: startDateMinus.value }
        return startDate
    }

    override suspend fun saveStartDate(value: Long) {
        dataStore.edit {
            it[START_DATE] = value
            startDateMinus.value = value
        }
    }

    override fun getUsername(): Flow<String> {
        val username = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[USER_NAME] ?: "" }
        return username
    }

    override suspend fun saveUsername(value: String) {
        dataStore.edit {
            it[USER_NAME] = value
        }
    }

    override fun getTerminator(): Flow<String> {
        val terminator = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[TERMINATOR] ?: "\n" }
        return terminator
    }

    override suspend fun saveTerminator(value: String) {
        dataStore.edit {
            it[TERMINATOR] = value
        }
    }

    override fun getUseNameInCount(): Flow<Boolean> {
        val useNameInCount = dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { it[USE_NAME_IN_COUNT] ?: false }
        return useNameInCount
    }

    override suspend fun saveUseNameInCount(value: Boolean) {
        dataStore.edit {
            it[USE_NAME_IN_COUNT] = value
        }
    }
}