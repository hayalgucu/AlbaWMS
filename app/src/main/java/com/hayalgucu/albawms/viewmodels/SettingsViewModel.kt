package com.hayalgucu.albawms.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.util.suffix
import com.hayalgucu.albawms.util.terminatorNames
import com.hayalgucu.albawms.util.terminatorValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val prefStore: PrefsStore
) : AndroidViewModel(application) {

    val selectedTerminator = mutableStateOf("")
    val keyboardOptions = mutableStateOf(false)
    val baseUrl = mutableStateOf("")


    init {
        getBaseUrl()
        getTerminator()
        getKeyboardOption()
    }

    private fun getBaseUrl() {
        viewModelScope.launch {
            prefStore.getServerAddress().collect {
                baseUrl.value = it
            }
        }
    }

    fun setBaseUrl(address: String) {
        viewModelScope.launch {
            prefStore.saveServerAddress(address)
        }
    }

    private fun getTerminator() {
        viewModelScope.launch {
            suffix = prefStore.getTerminator().first()
            val index = terminatorValues.indexOf(suffix)
            if (index >= 0) {
                selectedTerminator.value = terminatorNames[index]
            } else {
                selectedTerminator.value = terminatorNames[0]
            }
        }
    }

    fun saveTerminator(terminatorValue: String) {
        viewModelScope.launch {
            prefStore.saveTerminator(terminatorValue)
            suffix = terminatorValue
            selectedTerminator.value = terminatorNames[terminatorValues.indexOf(suffix)]
        }
    }

    private fun getKeyboardOption() {
        viewModelScope.launch {
            keyboardOptions.value = prefStore.getKeyboardOptions().first()
        }
    }

    fun saveKeyboardOption(keyboardOption: Boolean) {
        viewModelScope.launch {
            prefStore.saveKeyboardOptions(keyboardOption)
        }
    }

    fun saveInactivity(value: Long) {
        viewModelScope.launch {
            prefStore.saveInactivity(value)
        }
    }

    fun saveStartDate(value: Long) {
        viewModelScope.launch {
            prefStore.saveStartDate(value = value)
        }
    }

    fun saveAllSettings(inactivityText: String) {
        setBaseUrl(baseUrl.value)
        saveInactivity(inactivityText.toLong())
        saveKeyboardOption(keyboardOptions.value)
    }
}