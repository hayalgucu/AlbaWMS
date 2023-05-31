package com.hayalgucu.albawms.services.api

object HttpRoutes {

    //Login
    const val Login = "api/Login/Login"

    //Location
    const val GetShelfList = "api/Location/GetListShelf"
    const val GetLocationList = "api/Location/GetListLocation"
    const val GetItemsInLocation = "api/Location/GetStocksInLocation"
    const val GetLocation = "api/Location/GetLocation"

    //Machine
    const val GetMachineList = "api/Machine/GetMachineList"

    //Shelf
    const val GetShelf = "api/Shelf/GetShelf"
    const val ParkShelf = "api/Shelf/ParkShelf"

    //Stock
    const val GetItemByBarcode = "api/Stock/GetStockByBarcode"
    const val GetItemByCode = "api/Stock/GetStockByCode"

    const val GetItemListByCode = "api/Stock/GetListStockByCode"
    const val GetItemListByName = "api/Stock/GetListStockByName"

    const val GetItemLocationList = "api/Stock/GetListStockLocation"
    const val TakeItemFromShelf = "api/Stock/TakeStockFromShelf"

    const val SetConfirmationTakeItem = "api/Stock/SetConfirmationTakeStock"
    const val SetConfirmationItemPlacement = "api/Stock/SetConfirmationStockPlacement"
    const val PlaceStockToShelf = "api/Stock/PlacementStockToShelf"

}