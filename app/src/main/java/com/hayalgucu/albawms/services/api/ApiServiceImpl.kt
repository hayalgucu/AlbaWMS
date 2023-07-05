package com.hayalgucu.albawms.services.api

import com.hayalgucu.albawms.models.ErrorModel
import com.hayalgucu.albawms.models.GetItemModel
import com.hayalgucu.albawms.models.GetLocationListModel
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.models.ItemsInLocationModel
import com.hayalgucu.albawms.models.LocationInfoModel
import com.hayalgucu.albawms.models.LocationListModel
import com.hayalgucu.albawms.models.LocationModel
import com.hayalgucu.albawms.models.LoginModel
import com.hayalgucu.albawms.models.LoginResponseModel
import com.hayalgucu.albawms.models.MachineModel
import com.hayalgucu.albawms.models.ProgramUpdateModel
import com.hayalgucu.albawms.models.ResponseModel
import com.hayalgucu.albawms.models.ShelfModel
import com.hayalgucu.albawms.models.TakeItemConfirmationModel
import com.hayalgucu.albawms.util.machineList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ApiServiceImpl(private val url: String) : ApiService {

    private val client: HttpClient = HttpClientFactory().httpClient

    override suspend fun <T> getApiCall(function: suspend () -> ResponseModel<T>): ResponseModel<T> {
        val defaultResponse =
            ResponseModel<T>(null, 0, ErrorModel(listOf()), false)
        return try {
            function()
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            defaultResponse.statusCode = e.response.status.value
            defaultResponse.error!!.errors = listOf(e.response.status.description)

            defaultResponse
        } catch (e: ClientRequestException) {
            // 4xx - responses
            defaultResponse.statusCode = e.response.status.value
            defaultResponse.error!!.errors = listOf(e.response.status.description)

            defaultResponse
        } catch (e: ServerResponseException) {
            // 5xx - responses
            defaultResponse.statusCode = e.response.status.value
            defaultResponse.error!!.errors = listOf(e.response.status.description)

            defaultResponse
        } catch (e: Exception) {
            defaultResponse.statusCode = 0
            defaultResponse.error!!.errors = listOf(e.message ?: "")

            defaultResponse
        }
    }

    //Login
    override suspend fun login(loginModel: LoginModel): ResponseModel<LoginResponseModel> {
        return getApiCall {
            client.post("$url/${HttpRoutes.Login}") {
                setBody(loginModel)
            }.body()
        }
    }

    //Location
    override suspend fun getShelfList(machineList: Array<Int>): ResponseModel<List<ShelfModel>> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetShelfList}") {
                setBody(machineList)
            }.body()
        }
    }

    override suspend fun getLocationList(getLocationListModel: GetLocationListModel): ResponseModel<List<LocationModel>> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetLocationList}") {
                setBody(getLocationListModel)
            }.body()
        }
    }

    override suspend fun getItemsInLocation(location: String): ResponseModel<List<ItemsInLocationModel>> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetItemsInLocation}") {
                parameter("location", location)
            }.body()
        }
    }

    override suspend fun getLocation(location: String): ResponseModel<LocationInfoModel> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetLocation}") {
                parameter("location", location)
            }.body()
        }
    }

    override suspend fun getAllLocations(): ResponseModel<List<LocationListModel>> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetAllLocations}") {
                setBody(emptyArray<Int>())
            }.body()
        }
    }

    //Machine
    override suspend fun getMachineList(): ResponseModel<List<MachineModel>> {
        return getApiCall {
            client.get("$url/${HttpRoutes.GetMachineList}").body()
        }
    }

    //Shelf
    override suspend fun getShelf(getLocationListModel: GetLocationListModel): ResponseModel<String> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetShelf}") {
                setBody(getLocationListModel)
            }.body()
        }
    }

    override suspend fun parkShelf(machineNumber: Int): ResponseModel<Boolean> {
        return getApiCall {
            client.get("$url/${HttpRoutes.ParkShelf}") {
                parameter("machineNumber", machineNumber)
            }.body()
        }
    }

    //Stock
    override suspend fun getItemByBarcode(barcode: String): ResponseModel<ItemModel> {
        return getApiCall {
            client.get("$url/${HttpRoutes.GetItemByBarcode}") {
                parameter("barcode", barcode)
            }.body()
        }
    }

    override suspend fun getItemByCode(itemCode: String): ResponseModel<ItemModel> {
        return getApiCall {
            client.get("$url/${HttpRoutes.GetItemByCode}") {
                parameter("stockCode", itemCode)
            }.body()
        }
    }

    override suspend fun getItemListByCode(itemCode: String): ResponseModel<List<ItemModel>> {
        return getApiCall {
            client.get("$url/${HttpRoutes.GetItemListByCode}") {
                parameter("stockCode", itemCode)
            }.body()
        }
    }

    override suspend fun getItemListByName(itemName: String): ResponseModel<List<ItemModel>> {
        return getApiCall {
            client.get("$url/${HttpRoutes.GetItemListByName}") {
                parameter("name", itemName)
            }.body()
        }
    }

    //POST

    override suspend fun getItemLocationList(getItemModel: GetItemModel): ResponseModel<List<ItemLocationModel>> {
        return getApiCall {
            client.post("$url/${HttpRoutes.GetItemLocationList}") {
                setBody(getItemModel)
            }.body()
        }
    }

    override suspend fun takeItemFromShelf(itemLocationModel: ItemLocationModel): ResponseModel<Boolean> {
        return getApiCall {
            client.post("$url/${HttpRoutes.TakeItemFromShelf}") {
                setBody(itemLocationModel)
            }.body()
        }
    }

    override suspend fun setConfirmationTakeItem(takeItemConfirmationModel: TakeItemConfirmationModel): ResponseModel<Boolean> {
        return getApiCall {
            client.post("$url/${HttpRoutes.SetConfirmationTakeItem}") {
                setBody(takeItemConfirmationModel)
            }.body()
        }
    }

    override suspend fun setConfirmationItemPlacement(takeItemConfirmationModel: TakeItemConfirmationModel): ResponseModel<Boolean> {
        return getApiCall {
            client.post("$url/${HttpRoutes.SetConfirmationItemPlacement}") {
                setBody(takeItemConfirmationModel)
            }.body()
        }
    }

    override suspend fun placeStockToShelf(takeItemConfirmationModel: TakeItemConfirmationModel): ResponseModel<Boolean> {
        return getApiCall {
            client.post("$url/${HttpRoutes.PlaceStockToShelf}") {
                setBody(takeItemConfirmationModel)
            }.body()
        }
    }

    //App Info
    override suspend fun getAppVersion(appId: Int): ResponseModel<ProgramUpdateModel> {
        return getApiCall {
            client.get("") {
                parameter("id", appId)
            }.body()
        }
    }
}