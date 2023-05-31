package com.hayalgucu.albawms.services.api

import com.hayalgucu.albawms.models.GetItemModel
import com.hayalgucu.albawms.models.GetLocationListModel
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.models.ItemsInLocationModel
import com.hayalgucu.albawms.models.LocationInfoModel
import com.hayalgucu.albawms.models.LocationModel
import com.hayalgucu.albawms.models.LoginModel
import com.hayalgucu.albawms.models.LoginResponseModel
import com.hayalgucu.albawms.models.MachineModel
import com.hayalgucu.albawms.models.ResponseModel
import com.hayalgucu.albawms.models.ShelfModel
import com.hayalgucu.albawms.models.TakeItemConfirmationModel

interface ApiService {

    suspend fun <T> getApiCall(function: suspend () -> ResponseModel<T>): ResponseModel<T>

    //Login
    suspend fun login(loginModel: LoginModel): ResponseModel<LoginResponseModel>

    //Location
    suspend fun getShelfList(machineList: Array<Int>): ResponseModel<List<ShelfModel>>
    suspend fun getLocationList(getLocationListModel: GetLocationListModel): ResponseModel<List<LocationModel>>
    suspend fun getItemsInLocation(location: String): ResponseModel<List<ItemsInLocationModel>>
    suspend fun getLocation(location: String): ResponseModel<LocationInfoModel>

    //Machine
    suspend fun getMachineList(): ResponseModel<List<MachineModel>>

    //Shelf
    suspend fun getShelf(getLocationListModel: GetLocationListModel): ResponseModel<String>
    suspend fun parkShelf(machineNumber: Int): ResponseModel<Boolean>

    //Stock
    suspend fun getItemByBarcode(barcode: String): ResponseModel<ItemModel>
    suspend fun getItemByCode(itemCode: String): ResponseModel<ItemModel>

    suspend fun getItemListByCode(itemCode: String): ResponseModel<List<ItemModel>>
    suspend fun getItemListByName(itemName: String): ResponseModel<List<ItemModel>>

    suspend fun getItemLocationList(getItemModel: GetItemModel): ResponseModel<List<ItemLocationModel>>
    suspend fun takeItemFromShelf(itemLocationModel: ItemLocationModel): ResponseModel<Boolean>

    suspend fun setConfirmationTakeItem(takeItemConfirmationModel: TakeItemConfirmationModel): ResponseModel<Boolean>
    suspend fun setConfirmationItemPlacement(takeItemConfirmationModel: TakeItemConfirmationModel): ResponseModel<Boolean>
    suspend fun placeStockToShelf(takeItemConfirmationModel: TakeItemConfirmationModel): ResponseModel<Boolean>


    companion object {
        fun create(url: String) = ApiServiceImpl(url)
    }

}