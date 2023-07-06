package com.hayalgucu.albawms.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.models.GetItemModel
import com.hayalgucu.albawms.models.GetLocationInfoModel
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.models.TakeItemConfirmationModel
import com.hayalgucu.albawms.prefstore.PrefsStore
import com.hayalgucu.albawms.services.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemInfoViewModel @Inject constructor(
    private val apiService: ApiService,
    private val prefsStore: PrefsStore,
    application: Application
) : AndroidViewModel(application) {

    val succeed = mutableStateOf(false)
    val context = getApplication<Application>()

    var itemList = mutableStateOf<List<ItemModel>>(listOf())
    var itemLocationList = mutableStateOf<List<ItemLocationModel>>(listOf())
    var itemImageCount = mutableStateOf(0)
    var itemImageByteArray = mutableListOf<Byte>()//mutableStateOf(arrayListOf<Byte>())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    val selectedLocation = mutableStateOf<ItemLocationModel?>(null)

    var updateSuccess = mutableStateOf("")

    var noImage = mutableStateOf(false)

    var imageOne = mutableListOf<Byte>()
    var imageTwo = mutableListOf<Byte>()
    var noImages = mutableStateOf(false)

    val keyboardOptions = mutableStateOf(false)

    val shelfCame = mutableStateOf(false)

    private var userId = -1

    init {
        getKeyboardOptions()
    }

    private fun getUserId() {
        viewModelScope.launch {
            userId = prefsStore.getUserId().first().toInt()
        }
    }

    private fun getKeyboardOptions() {
        viewModelScope.launch {
            keyboardOptions.value = prefsStore.getKeyboardOptions().first()
        }
    }

    fun getShelf() {
        selectedLocation.value?.let { loc ->
            viewModelScope.launch {
                isLoading.value = true
                val machineNo = loc.hcrMakineNo
                val shelfNo = loc.hcrKonumNo
                val response = apiService.getShelf(GetLocationInfoModel(machineno = machineNo, shelfno = shelfNo))
                isLoading.value = false
                shelfCame.value = response.isSuccessful
                response.error?.errors?.let {
                    errorMessage.value = it.first()
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
                    shelfno = selectedLocation.value!!.hcrKonumNo,
                    userNo = userId
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

    fun searchProduct(query: String, searchType: String) {
        itemList.value = listOf()
        itemLocationList.value = listOf()
        when (searchType) {
            "barcode" -> {
                getItemByBarcode(query)
            }
            "item_code" -> {
                getItemByItemCode(query)
            }
            "item_name" -> {
                getItemByItemName(query)
            }
        }
    }

    fun getItemLocation(itemCode: String) {
        viewModelScope.launch {
            val response = apiService.getItemLocationList(GetItemModel(itemCode = itemCode, quantity = -1))
            response.data?.let {
                itemLocationList.value = it
            }
        }
    }

    private fun getItemByBarcode(query: String) {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.getItemByBarcode(query)

            isLoading.value = false

            if (response.isSuccessful) {
                response.data?.let {
                    itemList.value = listOf(it)
                } ?: kotlin.run {
                    errorMessage.value = response.error?.errors?.first() ?: "Not Found"
                }
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Not Found"
            }
        }
    }

    private fun getItemByItemCode(query: String) {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.getItemListByCode(query)

            isLoading.value = false

            if (response.isSuccessful) {
                response.data?.let {
                    if (it.isEmpty()) {
                      errorMessage.value = context.getString(R.string.item_not_found)
                    } else {
                        itemList.value = it
                    }
                } ?: kotlin.run {
                    errorMessage.value = response.error?.errors?.first() ?: "Not Found"
                }
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Not Found"
            }
        }
    }

    private fun getItemByItemName(query: String) {
        viewModelScope.launch {
            isLoading.value = true

            val response = apiService.getItemListByName(query)

            isLoading.value = false

            if (response.isSuccessful) {
                response.data?.let {
                    itemList.value = it
                } ?: kotlin.run {
                    errorMessage.value = response.error?.errors?.first() ?: "Not Found"
                }
            } else {
                errorMessage.value = response.error?.errors?.first() ?: "Not Found"
            }
        }
    }


}