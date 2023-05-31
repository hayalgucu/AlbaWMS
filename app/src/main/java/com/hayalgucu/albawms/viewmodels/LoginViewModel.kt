package com.hayalgucu.albawms.viewmodels

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.models.LoginModel
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.services.api.ApiService
import com.hayalgucu.albawms.util.machineList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val apiService: ApiService,
    private val prefsStore: PrefsStore
) : AndroidViewModel(application) {

    private val isUpdated = mutableStateOf(false)

    private val context get() = getApplication<Application>()

    val username = mutableStateOf("")

    private val path = context.filesDir
    private val localApkPath = "/" + "app.apk"

    private val manager = context.packageManager
    private var version = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        manager.getPackageInfo(
            context.packageName,
            PackageManager.GET_ACTIVITIES
        ).longVersionCode.toString()
    } else {
        "10"
    }

    val loginSucceed = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private val ftpServer = mutableStateOf("")
    private val ftpUser = mutableStateOf("")
    private val ftpPass = mutableStateOf("")

    init {
        getUsername()
    }

    private fun getUsername() {
        viewModelScope.launch {
            username.value = prefsStore.getUsername().first()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            val response = apiService.login(LoginModel(userName = username, password = password))
            isUpdated.value = false

            if (response.isSuccessful) {
                prefsStore.saveUsername(username)
                prefsStore.saveUser(response.data!!.usrRecno)
                getMachines()
            } else {
                errorMessage.value = response.error!!.errors.first()
            }
        }
    }
    
    private fun getMachines() {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.getMachineList()

            response.data?.let {
                machineList = it
            }

            isLoading.value = false
            loginSucceed.value = true
        }
    }

    fun getUpdateInfo() {
    }

}