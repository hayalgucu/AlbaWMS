package com.hayalgucu.albawms.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hayalgucu.albawms.models.MachineModel
import java.time.format.DateTimeFormatter

var scaffoldPadding = PaddingValues(0.dp)

var suffix = "\n"
val font = 18.sp

val terminatorNames = arrayListOf("LF", "CRLF", "CR", "TAB")
val terminatorValues = arrayListOf("\n", "\r\n", "\r", "\t")

val DEVICE_NAME = stringPreferencesKey("device_name")
val BLUETOOTH_DEVICE = stringPreferencesKey("bluetooth_device")
val PRINTER_NAME = stringPreferencesKey("printer_key")
val DEVICE_MODEL = stringPreferencesKey("device_model")
val USER_ID = stringPreferencesKey("user_id")
val USER_NAME = stringPreferencesKey("user_name")
val SERVER_TIME = stringPreferencesKey("server_time")
val FIRM_NAME = stringPreferencesKey("firm_name")
val IS_IMAGE_SHOWN = booleanPreferencesKey("is_image_shown")
val SERVER_ADDRESS = stringPreferencesKey("server_address")
val KEYBOARD_OPTIONS = booleanPreferencesKey("keyboard_options")
val INACTIVITY_TIME = longPreferencesKey("inactivity_time")
val MAC_ADDRESS = stringPreferencesKey("mac_address")
val ENTER_WAREHOUSE = intPreferencesKey("enter_warehouse")
val EXIT_WAREHOUSE = intPreferencesKey("exit_warehouse")
val START_DATE = longPreferencesKey("start_date")
val TERMINATOR = stringPreferencesKey("terminator")
val USE_NAME_IN_COUNT = booleanPreferencesKey("use_name_in_count")

val largeWidth = 120.dp
val midWidth = 80.dp
val smallWidth = 60.dp
val iconSize = 48.dp

val dummyTextForScan = mutableStateOf("")

val searchParams = listOf("item_code", "barcode", "item_name")

var inactivityTime = mutableStateOf(2L)
var startDateMinus = mutableStateOf(0L)
var pageText = mutableStateOf("")

val apiDateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

val apiDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

var machineList = emptyList<MachineModel>()