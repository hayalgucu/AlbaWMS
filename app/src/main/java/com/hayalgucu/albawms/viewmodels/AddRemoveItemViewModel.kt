package com.hayalgucu.albawms.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.models.GetItemModel
import com.hayalgucu.albawms.models.GetLocationListModel
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.models.LocationListModel
import com.hayalgucu.albawms.models.LocationModel
import com.hayalgucu.albawms.models.TakeItemConfirmationModel
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.services.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddRemoveItemViewModel @Inject constructor(
    private val apiService: ApiService,
    private val prefsStore: PrefsStore,
    application: Application
) : AndroidViewModel(application) {

    val showLocationDialog = mutableStateOf(false)
    val locationList = mutableStateListOf<LocationListModel>()

    val context = getApplication<Application>()

    val addEnabled = mutableStateOf(true)
    val removeEnabled = mutableStateOf(true)

    val locationText = mutableStateOf("")

    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    var item = mutableStateOf<ItemModel?>(null)
    var selectedLocation = mutableStateOf<ItemLocationModel?>(null)
    val itemLocations = mutableStateOf(listOf<ItemLocationModel>())
    var locations = mutableStateListOf<ItemLocationModel>()

    val productLocationQuantity = mutableStateOf(-1)
    var isLocationValid = mutableStateOf(false)

    val succeed = mutableStateOf(false)
    val keyboardOptions = mutableStateOf(false)

    val askForConfirmation = mutableStateOf(false)

    init {
        getKeyboardOptions()
    }

    private fun getKeyboardOptions() {
        viewModelScope.launch {
            keyboardOptions.value = prefsStore.getKeyboardOptions().first()
        }
    }

    fun confirmPlacement(stokCode: String, location: String, quantity: Int) {
        viewModelScope.launch {
            runBlocking {
                isLoading.value = true

                val segments = getLocationInfo(location)
                if (segments.isEmpty()) {
                    return@runBlocking
                }

                val response = apiService.setConfirmationItemPlacement(
                    TakeItemConfirmationModel(
                        stockcode = stokCode,
                        location = location,
                        quantity = quantity,
                        machineno = segments.first(),
                        shelfno = segments.last()
                    )
                )

                isLoading.value = false

                if (response.isSuccessful) {
                    errorMessage.value = ""
                    succeed.value = true
                } else {
                    errorMessage.value = response.error?.errors?.first() ?: "Error"
                }
            }
        }
    }

    fun confirmTake(stokCode: String, location: String, quantity: Int) {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.setConfirmationTakeItem(
                TakeItemConfirmationModel(
                    stockcode = stokCode,
                    location = location,
                    quantity = quantity,
                    machineno = selectedLocation.value!!.hcrMakineNo,
                    shelfno = selectedLocation.value!!.hcrKonumNo
                )
            )

            isLoading.value = false
            if (response.isSuccessful) {
                errorMessage.value = ""
                succeed.value = true
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Error"
            }

        }
    }

    fun placeItemToShelf(stokCode: String, location: String, quantity: Int) {
        viewModelScope.launch {
            runBlocking {
                isLoading.value = true

                val segments = getLocationInfo(location)
                if (segments.isEmpty()){
                    isLoading.value = false
                    return@runBlocking
                }

                val response = apiService.placeStockToShelf(
                    TakeItemConfirmationModel(
                        stockcode = stokCode,
                        location = location,
                        quantity = quantity,
                        machineno = segments.first(),
                        shelfno = segments.last()
                    )
                )

                isLoading.value = false

                if (response.isSuccessful) {
                    errorMessage.value = ""
                    askForConfirmation.value = true
                } else {
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

    fun takeItemFromShelf(stokCode: String, quantity: Int) {
        viewModelScope.launch {
            isLoading.value = true

            selectedLocation.value?.let {
                it.sipStokKodu = stokCode
                it.konumdanaldim = true
                it.alinancakMiktar = quantity.toDouble()
                val response = apiService.takeItemFromShelf(it)

                isLoading.value = false
                if (response.isSuccessful) {
                    errorMessage.value = ""
                    askForConfirmation.value = true
                } else {
                    errorMessage.value = response.error?.errors?.first() ?: "Error"
                }
            }
        }
    }

    fun getProductByStokCode(stokCode: String, resultValue: MutableState<ItemModel?>) {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.getItemByCode(stokCode)

            isLoading.value = false

            if (response.isSuccessful) {
                val result = response.data
                if (result != null) {
                    resultValue.value = result
                    if (resultValue.value!!.stkVarKonum != "") {
                        locationText.value = resultValue.value!!.stkVarKonum
                        getLocationStok(item.value!!.stkKodu, locationText.value)
/*                        if (removeEnabled.value)
                            getLocationStok(item.value!!.stkKodu, locationText.value)
                        else if (addEnabled.value)
                            checkIsLocationValid(locationText.value)*/
                    }
                    getLocation()
                } else {
                    resultValue.value = null
                    errorMessage.value = context.getString(R.string.item_not_found)
                }
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Error"
            }
        }
    }

    fun checkIsLocationValid(location: String) {
        viewModelScope.launch {
            isLoading.value = true
            //TODO(KONUM MAKINA VE RAF BILGISI AL ICIN DEGISTIR)
            val response = apiService.getItemsInLocation(location)

            isLoading.value = false

            if (!response.isSuccessful) {
                errorMessage.value = response.error!!.errors.first()
            } else {
                isLocationValid.value = true
            }
        }
    }

    fun getLocationStok(stokCode: String, location: String) {
        viewModelScope.launch {
            isLoading.value = true
//            selectedLocation.value = null

            val response =
                apiService.getItemLocationList(GetItemModel(itemCode = stokCode, quantity = -1))

            isLoading.value = false

            if (response.isSuccessful) {
                response.data?.let { locations ->
                    productLocationQuantity.value =
                        locations.count { L -> L.altkonum == location }
                    when (productLocationQuantity.value) {
                        0 -> {
                            errorMessage.value = context.getString(R.string.item_notin_location)
                            productLocationQuantity.value = -1
                            selectedLocation.value = null
                        }

                        1 -> {
                            selectedLocation.value =
                                locations.first { L -> L.altkonum == location }
                        }

                        else -> {
                            return@launch
                        }
                    }
                }
            } else {
                selectedLocation.value = null
                errorMessage.value = response.error?.errors?.first() ?: "Error"
            }
        }
    }

    private fun getLocation() {
        viewModelScope.launch {
            val response = apiService.getItemLocationList(
                GetItemModel(
                    itemCode = item.value!!.stkKodu,
                    quantity = -1
                )
            )
            if (response.isSuccessful) {
                response.data?.let {
                    val list = it.toMutableStateList()
                    list.sortBy { L -> L.altkonum }
                    itemLocations.value = list
                    locations = list
                } ?: kotlin.run {
                    itemLocations.value = listOf()
                }
            } else {
                itemLocations.value = listOf()
            }
        }
    }

    fun getAllLocations() {
        viewModelScope.launch {
            locationList.clear()
            val response = apiService.getAllLocations()

            if (response.isSuccessful) {
                isLoading.value = false

                response.data?.let { locationList.addAll(it) }

                showLocationDialog.value = true
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Error"
            }
        }
    }
}