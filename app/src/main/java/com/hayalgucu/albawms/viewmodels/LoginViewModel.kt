package com.hayalgucu.albawms.viewmodels

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.BuildConfig
import com.hayalgucu.albawms.models.LoginModel
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.services.api.ApiService
import com.hayalgucu.albawms.util.inactivityTime
import com.hayalgucu.albawms.util.machineList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val apiService: ApiService,
    private val prefsStore: PrefsStore
) : AndroidViewModel(application) {

    private val isUpdated = mutableStateOf(false)
    private val updateDone = mutableStateOf(false)

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
        getInactivity()
    }

    private fun getInactivity() {
        viewModelScope.launch {
            inactivityTime.value = prefsStore.getInactivity().first()
        }
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

    private val downloadApkRunnable = Runnable {
        kotlin.run {
            val client = FTPClient()
            val apkName = "albawms.apk"

            try {
                client.connect(ftpServer.value)
                val login = client.login(ftpUser.value, ftpPass.value)

                if (login) {
                    client.setFileType(FTP.BINARY_FILE_TYPE)
                    client.enterLocalPassiveMode()

                    var outStream: OutputStream? = null
                    var success = false

                    try {
                        val outFile = File(path, localApkPath)

                        outStream = BufferedOutputStream(FileOutputStream(outFile))
                        success = client.retrieveFile(apkName, outStream)
                        if (success)
                            installApk()
                        else
                            println(client.replyString)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        outStream?.close()
                    }
                } else {
                    println(client.replyString)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    client.logout()
                    client.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun installApk() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                updateDone.value = true
            }
        }
        val contentUri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            File(path, localApkPath)
        )

        val install = Intent(Intent.ACTION_VIEW)
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        install.data = contentUri
        context.startActivity(install)
    }

    fun getUpdateInfo() {
        viewModelScope.launch {
            val response = apiService.getAppVersion(5)

            if (response.isSuccessful) {
                response.data?.let {
                    if (it.version.toLong() > version.toLong()) {
                        ftpServer.value = it.ftpServer
                        ftpUser.value = it.ftpUser
                        ftpPass.value = it.ftpPass
                        Thread(downloadApkRunnable).start()
                        withContext(Dispatchers.Main) {
                            isUpdated.value = false
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            isUpdated.value = true
                            updateDone.value = true
                        }
                    }
                }
            } else {
                isUpdated.value = true
                updateDone.value = true
            }
        }
    }

}