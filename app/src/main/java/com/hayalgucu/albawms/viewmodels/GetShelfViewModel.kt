package com.hayalgucu.albawms.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.models.GetLocationListModel
import com.hayalgucu.albawms.services.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetShelfViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    val context = getApplication<Application>()

    var shelfNo = mutableStateOf("")

    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun getShelf(machineNo: Int) {
        viewModelScope.launch {
            val shelfInt = shelfNo.value.toIntOrNull()
            if (shelfInt == null) {
                errorMessage.value = context.getString(R.string.wrong_location)
                shelfNo.value = ""
                return@launch
            }
            isLoading.value = true

            val response = apiService.getShelf(GetLocationListModel(machineno = machineNo, shelfno = shelfInt))
            shelfNo.value = ""

            isLoading.value = false

            if (!response.isSuccessful) {
                errorMessage.value = response.error!!.errors.first()
            }
        }
    }

    fun parkShelf(machineNo: Int) {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.parkShelf(machineNo)

            isLoading.value = false

            if (!response.isSuccessful) {
                errorMessage.value = response.error!!.errors.first()
            }
        }
    }
}