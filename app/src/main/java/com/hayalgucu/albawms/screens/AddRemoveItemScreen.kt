package com.hayalgucu.albawms.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.CustomIcon
import com.hayalgucu.albawms.customviews.ItemLocationList
import com.hayalgucu.albawms.customviews.TableCell
import com.hayalgucu.albawms.customviews.TableHeaderCell
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.ui.theme.Primary200
import com.hayalgucu.albawms.util.ScannerOptions
import com.hayalgucu.albawms.util.largeWidth
import com.hayalgucu.albawms.util.scaffoldPadding
import com.hayalgucu.albawms.util.suffix
import com.hayalgucu.albawms.util.toItemLocationModel
import com.hayalgucu.albawms.viewmodels.AddRemoveItemViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddRemoveItemScreen(
    viewModel: AddRemoveItemViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val itemFocusRequester = remember { FocusRequester() }
//    val locationFocusRequester = remember { FocusRequester() }
    val quantityFocusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    var doneEnable by remember { mutableStateOf(false) }

    val itemEnabled = remember { mutableStateOf(false) }
    val locationEnabled = remember { mutableStateOf(false) }
    val quantityEnabled = remember { mutableStateOf(false) }

    var addEnabled by remember { viewModel.addEnabled }
    var removeEnabled by remember { viewModel.removeEnabled }

    var selectedLocation = remember { viewModel.selectedLocation }

    val itemText = remember { mutableStateOf("") }
    val locationText = remember { viewModel.locationText }
    val quantityText = remember { mutableStateOf("") }

    val itemLocations by remember { viewModel.itemLocations }

    var errorMessage by remember { viewModel.errorMessage }
    var isLoading by remember { viewModel.isLoading }

    val item by remember { viewModel.item }

    val productLocationQuantity by remember { viewModel.productLocationQuantity }
    val isLocationValid by remember { viewModel.isLocationValid }

    var askForConfirm by remember { viewModel.askForConfirmation }
    val isSuccess by remember { viewModel.succeed }

    val keyboardOptions by remember { viewModel.keyboardOptions }

    var showLocationDialog by remember { viewModel.showLocationDialog }
    var locationList = remember { viewModel.locationList }

    var showMinMaxWarning by remember { mutableStateOf(false) }


    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, context.getString(R.string.process_succeed), Toast.LENGTH_SHORT)
                .show()
            viewModel.succeed.value = false

            addEnabled = true
            removeEnabled = true

            itemEnabled.value = true
            locationEnabled.value = true
            quantityEnabled.value = true

            itemText.value = ""
            locationText.value = ""
            quantityText.value = ""

            viewModel.item.value = null
            viewModel.productLocationQuantity.value = -1
            viewModel.isLocationValid.value = false
            viewModel.itemLocations.value = arrayListOf()

            doneEnable = false
        }
    }


    LaunchedEffect(key1 = addEnabled, key2 = removeEnabled) {
        if (addEnabled && !removeEnabled) {
            itemEnabled.value = true
            locationEnabled.value = true
            quantityEnabled.value = true
        } else if (!addEnabled && removeEnabled) {
            itemEnabled.value = true
            locationEnabled.value = true
            quantityEnabled.value = true
        } else {
            itemEnabled.value = false
            locationEnabled.value = false
            quantityEnabled.value = false
        }
    }

    LaunchedEffect(itemEnabled.value, locationEnabled.value, quantityEnabled.value) {
        if (itemEnabled.value) {
            itemFocusRequester.requestFocus()
        } /*else if (locationEnabled.value) {
            locationFocusRequester.requestFocus()
        }*/ else if (quantityEnabled.value) {
            quantityFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(selectedLocation.value) {
        selectedLocation.value?.let {
            locationText.value = it.altkonum
            if (removeEnabled)
                viewModel.getLocationStok(item!!.stkKodu, locationText.value)
            else if (addEnabled)
                viewModel.checkIsLocationValid(locationText.value)
        } ?: kotlin.run {
            locationText.value = ""
        }
    }

    AlbaWMSTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 5.dp)
        ) {
            if (isLoading) {
                Dialog(onDismissRequest = {}) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            }

            if (showMinMaxWarning) {
                AlertDialog(
                    onDismissRequest = { showMinMaxWarning = false },
                    title = { Text(text = stringResource(id = R.string.warning)) },
                    text = {
                        when {
                            addEnabled && !removeEnabled -> {
                                Text(text = stringResource(R.string.above_max))
                            }

                            !addEnabled && removeEnabled -> {
                                Text(text = stringResource(R.string.below_min))
                            }

                            else -> {
                                Text(text = "")
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            showMinMaxWarning = false
                            if (addEnabled && !removeEnabled) {
                                viewModel.placeItemToShelf(
                                    itemText.value,
                                    locationText.value,
                                    quantityText.value.toInt()
                                )
                            } else if (!addEnabled && removeEnabled) {
                                viewModel.takeItemFromShelf(
                                    itemText.value,
                                    quantityText.value.toInt()
                                )
                            }
                        }) {
                            Text(text = stringResource(id = R.string.ok))
                        }
                    },
                    dismissButton = {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                    }
                )
            }

            if (errorMessage.isNotEmpty()) {
                AlertDialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    ),
                    title = { Text(stringResource(id = R.string.error)) },
                    text = {
                        when {
                            errorMessage.contains("Error") -> Text(stringResource(R.string.something_went_wrong))
                            else -> Text(errorMessage)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (errorMessage == "Seçilen konumda seçili ürün yok.") {
                                viewModel.productLocationQuantity.value = -1
                                locationText.value = ""
                            } else if (errorMessage.contains("Stok Bulunamadı")) {
                                itemText.value = ""
                            } else if (errorMessage.contains("Konum")) {
                                locationText.value = ""
                            } else if (errorMessage.contains("Alan") || errorMessage.contains("Taşınacak miktarda")) {
                                quantityText.value = ""
                            } else {
                                addEnabled = true
                                removeEnabled = true

                                itemEnabled.value = true
                                locationEnabled.value = true
                                quantityEnabled.value = true

                                itemText.value = ""
                                locationText.value = ""
                                quantityText.value = ""

                                viewModel.item.value = null
                                viewModel.productLocationQuantity.value = -1
                                viewModel.isLocationValid.value = false
                                viewModel.itemLocations.value = arrayListOf()

                                doneEnable = false
                            }
                            viewModel.errorMessage.value = ""
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    }
                )
            }

            if (askForConfirm) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = stringResource(id = R.string.warning)) },
                    text = {
                        if (addEnabled && !removeEnabled) {
                            Text(
                                stringResource(
                                    id = R.string.confirm_placement,
                                    itemText.value,
                                    locationText.value,
                                    quantityText.value
                                )
                            )
                        } else {
                            Text(
                                stringResource(
                                    id = R.string.confirm_take,
                                    itemText.value,
                                    locationText.value,
                                    quantityText.value
                                )
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (addEnabled && !removeEnabled)
                                viewModel.confirmPlacement(
                                    itemText.value,
                                    locationText.value,
                                    quantityText.value.toInt()
                                )
                            else
                                viewModel.confirmTake(
                                    itemText.value,
                                    locationText.value,
                                    quantityText.value.toInt()
                                )
                            askForConfirm = false
                        }) {
                            Text(text = stringResource(R.string.confirm))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            addEnabled = true
                            removeEnabled = true

                            itemEnabled.value = false
                            locationEnabled.value = false
                            quantityEnabled.value = false

                            itemText.value = ""
                            locationText.value = ""
                            quantityText.value = ""

                            viewModel.item.value = null
                            viewModel.productLocationQuantity.value = -1
                            viewModel.isLocationValid.value = false
                            viewModel.itemLocations.value = arrayListOf()
                            viewModel.selectedLocation.value = null

                            doneEnable = false

                            askForConfirm = false
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                )
            }

            if (showLocationDialog) {
                Dialog(onDismissRequest = {
                    showLocationDialog = false
                }) {
                    Surface() {
                        Column(Modifier.padding(5.dp)) {
                            LazyColumn() {
                                item {
                                    Row(Modifier.background(Primary200)) {
                                        TableHeaderCell(
                                            text = stringResource(id = R.string.location),
                                            largeWidth
                                        )
                                    }
                                }
                                items(locationList) { rowItem ->
                                    Row(
                                        Modifier
                                            .clickable {
                                                locationText.value = rowItem.location
                                                selectedLocation.value =
                                                    rowItem.toItemLocationModel()
                                                showLocationDialog = false
                                            }) {
                                        TableCell(text = rowItem.location, largeWidth)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = { removeEnabled = false }, enabled = addEnabled) {
                    Icon(
                        Icons.Rounded.AddCircleOutline,
                        contentDescription = "Ekleme",
                        Modifier.size(48.dp),
                        tint = if (addEnabled) Color.Green else Color.Gray
                    )
                }
                IconButton(onClick = { addEnabled = false }, enabled = removeEnabled) {
                    Icon(
                        Icons.Rounded.RemoveCircleOutline,
                        contentDescription = "Cikarma",
                        Modifier.size(48.dp),
                        tint = if (removeEnabled) Color.Red else Color.Gray
                    )
                }
                IconButton(onClick = {
                    addEnabled = true
                    removeEnabled = true

                    itemEnabled.value = false
                    locationEnabled.value = false
                    quantityEnabled.value = false

                    itemText.value = ""
                    locationText.value = ""
                    quantityText.value = ""

                    viewModel.item.value = null
                    viewModel.productLocationQuantity.value = -1
                    viewModel.isLocationValid.value = false
                    viewModel.itemLocations.value = arrayListOf()
                    viewModel.selectedLocation.value = null

                    doneEnable = false
                }) {
                    CustomIcon(icon = Icons.Rounded.InsertDriveFile, cd = "Yeni")
                }
                IconButton(onClick = {
                    if (itemText.value != "") {
                        viewModel.getProductByStokCode(
                            itemText.value,
                            viewModel.item
                        )
                    }
                }) {
                    CustomIcon(icon = Icons.Rounded.Search, cd = "Arama")
                }
                IconButton(onClick = {
                    if (addEnabled && !removeEnabled) {
                        if (item!!.depoMiktari + quantityText.value.toDouble() > item!!.stkMaxSeviye && item!!.stkMaxSeviye != 0.0) {
                            showMinMaxWarning = true
                        } else {
                            viewModel.placeItemToShelf(
                                itemText.value,
                                locationText.value,
                                quantityText.value.toInt()
                            )
                        }
                    } else if (removeEnabled && !addEnabled) {
                        if (item!!.depoMiktari - quantityText.value.toDouble() < item!!.stkKritikSeviye && item!!.stkKritikSeviye != 0.0) {
                            showMinMaxWarning = true
                        } else {
                            viewModel.takeItemFromShelf(
                                itemText.value,
                                quantityText.value.toInt()
                            )
                        }
                    }
                }, enabled = doneEnable) {
                    CustomIcon(icon = Icons.Rounded.Done, cd = "Onayla", enabled = doneEnable)
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(itemFocusRequester)
                    .onFocusEvent {
                        if (it.isFocused && !keyboardOptions) {
                            keyboardController?.hide()
                        }
                    },
                value = itemText.value,
                onValueChange = {
                    ScannerOptions(
                        inputString = it,
                        searchText = itemText,
                        keyboardOptions = keyboardOptions,
                        afterSuffixFunc = {
                            if (itemText.value != "") {
                                viewModel.getProductByStokCode(
                                    itemText.value,
                                    viewModel.item
                                )
                            }
                        }
                    )
                },
                label = { Text(text = stringResource(R.string.item_code)) },
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                singleLine = true,
                enabled = itemEnabled.value
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (!removeEnabled && addEnabled && item != null) {
                            isLoading = true
                            viewModel.getAllLocations()
                        }
                    }
                /*.focusRequester(locationFocusRequester)
                .onFocusEvent {
                    if (it.isFocused && !keyboardOptions) {
                        keyboardController?.hide()
                    }
                }*/,
                value = locationText.value,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.location)) },
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                /*                singleLine = true,
                                keyboardActions = KeyboardActions(onDone = {
                                    if (item != null && locationText.value != "") {
                                        if (removeEnabled)
                                            viewModel.getLocationStok(item!!.stkKodu, locationText.value)
                                        else if (addEnabled)
                                            viewModel.checkIsLocationValid(locationText.value)
                                    }
                                }),*/
                enabled = false // locationEnabled.value
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(quantityFocusRequester),
                value = quantityText.value,
                onValueChange = {
                    if (selectedLocation.value == null) {
                        errorMessage = context.getString(R.string.no_location_selected)
                        return@OutlinedTextField
                    }
                    if (it.matches("-?\\d+(\\.\\d+)?".toRegex()) || it == "")
                        quantityText.value = it.replace(suffix, "")
                    if (it.contains(Regex(suffix))) {
                        focusManager.clearFocus()
                        quantityText.value = it.replace(suffix, "")
                        if (quantityText.value != "") {
                            if (removeEnabled && !addEnabled) {
                                if (quantityText.value.toInt() <= viewModel.selectedLocation.value!!.sipKalan) {
                                    doneEnable = true
                                    quantityEnabled.value = false
                                } else {
                                    viewModel.errorMessage.value =
                                        if (quantityText.value == "") context.getString(R.string.field_cannot_blank)
                                        else if (quantityText.value.toInt() == 0) context.getString(
                                            R.string.move_quant_mustbe_greater_zero
                                        )
                                        else context.getString(R.string.not_enough_item_for_move)
                                }
                            } else if (addEnabled && !removeEnabled) {
                                if (quantityText.value.toInt() > 0) {
                                    doneEnable = true
                                    quantityEnabled.value = false
                                } else {
                                    viewModel.errorMessage.value =
                                        if (quantityText.value == "") context.getString(R.string.field_cannot_blank)
                                        else context.getString(R.string.add_quant_mustbe_greater_zero)
                                }
                            }
                        }
                    }
                },
                label = { Text(text = stringResource(R.string.quantity)) },
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    if (selectedLocation.value == null) {
                        errorMessage = context.getString(R.string.no_location_selected)
                        return@KeyboardActions
                    }
                    //Check Miktar
                    if (quantityText.value != "") {
                        focusManager.clearFocus()
                        if (removeEnabled && !addEnabled) {
                            if (quantityText.value.toInt() <= viewModel.selectedLocation.value!!.sipKalan) {
                                doneEnable = true
                                quantityEnabled.value = false
                            } else {
                                viewModel.errorMessage.value =
                                    if (quantityText.value == "") context.getString(R.string.field_cannot_blank)
                                    else if (quantityText.value.toInt() == 0) context.getString(R.string.move_quant_mustbe_greater_zero)
                                    else context.getString(R.string.not_enough_item_for_move)
                            }
                        } else if (addEnabled && !removeEnabled) {
                            if (quantityText.value.toInt() > 0) {
                                doneEnable = true
                                quantityEnabled.value = false
                            } else {
                                viewModel.errorMessage.value =
                                    if (quantityText.value == "") context.getString(R.string.field_cannot_blank)
                                    else context.getString(R.string.add_quant_mustbe_greater_zero)
                            }
                        }
                    }
                }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = quantityEnabled.value
            )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            if (item != null) {
                itemEnabled.value = false
            }

            if ((productLocationQuantity > 0 || isLocationValid) && locationEnabled.value) {
                locationEnabled.value = false
            }

            if (itemLocations.isNotEmpty()) {
                //List
                ItemLocationList(
                    itemLocationModelList = viewModel.locations,
                    selectedLocation,
                    isClickable = true
                )
            }
        }
    }
}