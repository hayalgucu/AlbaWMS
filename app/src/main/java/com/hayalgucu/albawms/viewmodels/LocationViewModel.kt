package com.hayalgucu.albawms.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.models.GetLocationInfoModel
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.models.ItemsInLocationModel
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.services.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val apiService: ApiService,
    private val prefsStore: PrefsStore
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    val selectedLocation = mutableStateOf("")

    val selectedItem = mutableStateOf<ItemModel?>(null)
    val selectedOldLocation = mutableStateOf("")
    val selectedNewItem = mutableStateOf("")
    val quantity = mutableStateOf(1)

    var locationItemList = mutableStateListOf<ItemsInLocationModel>()

    fun getLocationItems(location: String) {
        viewModelScope.launch {
            locationItemList.clear()
            selectedLocation.value = ""
            isLoading.value = true

            val response = apiService.getItemsInLocation(location)

            isLoading.value = false

            if (response.isSuccessful) {
                response.data?.let { list ->
                    selectedLocation.value = location
                    locationItemList.addAll(list)
                }
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Error"
            }
        }
    }

    fun callShelf() {
        viewModelScope.launch {
            runBlocking {
                isLoading.value = true
                val segments = getLocationInfo(selectedLocation.value)
                if (segments.isEmpty()) {
                    isLoading.value = false
                    return@runBlocking
                }
                val response = apiService.getShelf(
                    GetLocationInfoModel(
                        machineno = segments.first(),
                        shelfno = segments.last()
                    )
                )
                isLoading.value = false
                if (!response.isSuccessful) {
                    errorMessage.value = response.error?.errors?.first() ?: "Error"
                }
            }
        }
    }

    private suspend fun getLocationInfo(location: String): List<Int> {
        val response = apiService.getLocation(location)
        return if (response.isSuccessful) {
            listOf(response.data!!.machineNumber, response.data.shelfNo)
        } else {
            errorMessage.value = response.error?.errors?.first() ?: "Error"
            emptyList()
        }
    }
}