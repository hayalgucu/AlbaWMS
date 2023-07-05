package com.hayalgucu.albawms.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Unarchive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.CustomDialog
import com.hayalgucu.albawms.customviews.CustomIcon
import com.hayalgucu.albawms.customviews.ItemLocationList
import com.hayalgucu.albawms.customviews.ItemSearchBox
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.ScannerOptions
import com.hayalgucu.albawms.util.ShowAlertDialog
import com.hayalgucu.albawms.util.scaffoldPadding
import com.hayalgucu.albawms.util.searchParams
import com.hayalgucu.albawms.viewmodels.ItemInfoViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemInfoScreen(
    viewModel: ItemInfoViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()

    val searchText = remember { mutableStateOf("") }
    val categoryIdx = remember { mutableStateOf(0) }
    val emptyTextError = remember { mutableStateOf("") }
    var isLocationTried by remember { mutableStateOf(false) }
    val imageDialogVisible = remember { mutableStateOf(false) }
    val imageIdx = remember { mutableStateOf(1) }

    val itemList by remember { viewModel.itemList }
    val itemLocationModelList by remember { viewModel.itemLocationList }
    val itemImageCount = remember { viewModel.itemImageCount }
    val imageByteArray = remember { viewModel.itemImageByteArray }
    val errorMessage by remember { viewModel.errorMessage }
    var isLoading by remember { viewModel.isLoading }

    val keyboardOptions by remember { viewModel.keyboardOptions }

    val selectedItem = remember { mutableStateOf<ItemModel?>(null) }

    val selectedLocation = remember { viewModel.selectedLocation }

    var imageBtnEnabled by remember { mutableStateOf(false) }

    var shelfCame by remember { viewModel.shelfCame }
    var showAmountDialog by remember { mutableStateOf(false) }
    var amountText by remember { mutableStateOf("") }
    var succeed by remember { viewModel.succeed }

    LaunchedEffect(shelfCame) {
        if (shelfCame) {
            shelfCame = false
            showAmountDialog = true
        }
    }

    LaunchedEffect(succeed) {
        if (succeed) {
            Toast.makeText(context, context.getString(R.string.process_succeed), Toast.LENGTH_SHORT).show()
            viewModel.succeed.value = false

            viewModel.searchProduct(
                selectedItem.value!!.stkKodu,
                searchParams[0]
            )

            isLocationTried = false

            showAmountDialog = false
        }
    }

    LaunchedEffect(showAmountDialog) {
        if (!showAmountDialog)
            amountText = ""
    }

    AlbaWMSTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 5.dp)
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    Dialog(onDismissRequest = {}) {
                        CircularProgressIndicator()
                    }
                }
                if (errorMessage == stringResource(id = R.string.item_not_found)) {
                    AlertDialog(
                        onDismissRequest = {
                            viewModel.errorMessage.value = ""
                            searchText.value = ""
                        },
                        title = { Text(stringResource(id = R.string.warning)) },
                        text = { Text(stringResource(R.string.item_not_found)) },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.errorMessage.value = ""
                                searchText.value = ""
                            }) {
                                Text(stringResource(id = R.string.ok))
                            }
                        }
                    )
                } else if (errorMessage.isNotEmpty()) {
                    ShowAlertDialog(errorState = viewModel.errorMessage, text = errorMessage)
                }

                if (emptyTextError.value.isNotEmpty()) {
                    ShowAlertDialog(
                        text = stringResource(R.string.search_can_not_blank),
                        errorState = emptyTextError
                    )
                }

                if (showAmountDialog) {
                    Dialog(onDismissRequest = { showAmountDialog = false }) {
                        Column(
                            modifier = Modifier
                                .background(Color.Black)
                                .width(512.dp)
                                .padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.ask_amount,
                                    selectedItem.value!!.stkKodu,
                                    selectedLocation.value!!.altkonum
                                )
                            )

                            OutlinedTextField(
                                value = amountText,
                                onValueChange = { amountText = it },
                                label = { Text(text = stringResource(R.string.quantity)) })

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(onClick = { showAmountDialog = false }) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                                Button(onClick = {
                                    if (amountText.toInt() <= viewModel.selectedLocation.value!!.sipKalan) {
                                        viewModel.confirmTake(
                                            selectedItem.value!!.stkKodu,
                                            selectedLocation.value!!.altkonum,
                                            amountText.toInt()
                                        )
                                    } else {
                                        showAmountDialog = false
                                        viewModel.errorMessage.value =
                                            if (amountText == "") context.getString(R.string.field_cannot_blank)
                                            else if (amountText.toInt() == 0) context.getString(R.string.move_quant_mustbe_greater_zero)
                                            else context.getString(R.string.not_enough_item_for_move)
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.confirm))
                                }
                            }
                        }
                    }
                }

                Column(
                    Modifier.fillMaxSize(),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        val searchBoxItems = listOf(
                            stringResource(id = R.string.item_code),
                            stringResource(R.string.barcode),
                            stringResource(
                                id = R.string.item_name
                            )
                        )
                        ItemSearchBox(
                            fulfillment = .8f,
                            searchText = searchText,
                            labelList = searchBoxItems,
                            labelIdx = categoryIdx,
                            onKeyboardDone = {
                                if (categoryIdx.value == searchBoxItems.size - 1 || keyboardOptions) {
                                    isLocationTried = false
                                    focusManager.clearFocus()
                                    selectedItem.value = null
                                    viewModel.itemImageCount.value = 0
                                    viewModel.itemImageByteArray.clear()

                                    if (searchText.value.isBlank()) {
                                        emptyTextError.value = "aaaaaa"
                                    } else {
                                        viewModel.searchProduct(
                                            searchText.value,
                                            searchParams[categoryIdx.value]
                                        )
                                    }
                                }
                            },
                            onValueChangeListener = {
                                ScannerOptions(
                                    inputString = it,
                                    categoryIdx = categoryIdx,
                                    searchBoxItems = searchBoxItems,
                                    searchText = searchText,
                                    keyboardOptions = keyboardOptions,
                                    afterSuffixFunc = {
                                        focusManager.clearFocus(true)
                                        isLocationTried = false
                                        focusManager.clearFocus()
                                        selectedItem.value = null
                                        viewModel.itemImageCount.value = 0
                                        viewModel.itemImageByteArray.clear()

                                        if (searchText.value.isBlank()) {
                                            emptyTextError.value = "aaaaaa"
                                        } else {
                                            viewModel.searchProduct(
                                                searchText.value,
                                                searchParams[categoryIdx.value]
                                            )
                                        }
                                    }
                                )
                            },
                            focusRequester = focusRequester,
                            keyboardController = keyboardController,
                            keyboardOptions = keyboardOptions
                        )
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp),
                            onClick = {
                                isLocationTried = false
                                focusManager.clearFocus()
                                selectedItem.value = null
                                viewModel.itemImageCount.value = 0
                                viewModel.itemImageByteArray.clear()

                                if (searchText.value.isBlank()) {
                                    emptyTextError.value = "aaaaaa"
                                } else {
                                    viewModel.searchProduct(
                                        searchText.value,
                                        searchParams[categoryIdx.value]
                                    )
                                }
                            }) {
                            CustomIcon(icon = Icons.Rounded.Search, cd = "Arama")
                        }
                    }

                    if (itemList.size == 1 && !isLocationTried) {
                        selectedItem.value = itemList.first()
                        viewModel.getItemLocation(selectedItem.value!!.stkKodu)
//                        viewModel.getStokImageCount(selectedProduct.value!!.sto_Guid)
                        isLocationTried = true
                    } else if (itemList.size > 1) {
                        CustomDialog(
                            itemList = itemList,
                            selectedItem = selectedItem,
                            searchText = searchText
                        )
                        isLocationTried = false
                    }

                    SideEffect {
                        if (searchText.value.isBlank() && emptyTextError.value.isBlank()) {
                            try {
                                focusRequester.requestFocus()
                            } catch (e: Exception) {
                                println(e.localizedMessage)
                            }
                            keyboardController?.hide()
                        }
                        if (searchText.value.isNotBlank() && selectedItem.value != null) {
                            searchText.value = ""
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(
                                id = R.string.item_code_params,
                                selectedItem.value?.stkKodu ?: ""
                            )
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(
                                id = R.string.warehouse_quantity_params,
                                selectedItem.value?.depoMiktari ?: "0"
                            )
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        selectedItem.value?.stkAdi?.let {
                            println(it.length)
                            Text(
                                text = if (it.length > 45) stringResource(
                                    id = R.string.item_name_params,
                                    it.subSequence(0, 45)
                                )
                                else stringResource(id = R.string.item_name_params, it)
                            )
                        } ?: kotlin.run {
                            Text("Stok Adı: ")
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            searchText.value = ""
                            isLocationTried = false
                            selectedItem.value = null
                            viewModel.itemList.value = listOf()
                            viewModel.itemLocationList.value = listOf()
                            viewModel.itemImageCount.value = 0
                            viewModel.itemImageByteArray.clear()
                        }) {
                            CustomIcon(icon = Icons.Rounded.InsertDriveFile, cd = "Temizle")
                        }

                        IconButton(onClick = {
                            viewModel.getShelf()
                        }, enabled = selectedLocation.value != null) {
                            CustomIcon(
                                icon = Icons.Rounded.Unarchive,
                                cd = "Bring Tray",
                                enabled = selectedLocation.value != null
                            )
                        }

                        IconButton(onClick = {
                            /*isLoading = true
                            viewModel.getStokImageOneFunc(
                                selectedProduct.value!!.sto_kod,
                                imageIdx.value.toString(),
                                imageDialogVisible
                            )
                            imageDialogVisible.value = true*/
                        }, enabled = imageBtnEnabled) {
                            CustomIcon(
                                icon = Icons.Rounded.Image,
                                cd = "Fotograflar",
                                enabled = imageBtnEnabled
                            )
                        }
                    }
                    //List
                    ItemLocationList(
                        itemLocationModelList = itemLocationModelList,
                        selectedLocation,
                        true
                    )

                }
                imageBtnEnabled = itemImageCount.value != 0

                /*                if (imageByteArray.value.isNotEmpty() && imageDialogVisible.value) {
                                    isLoading = false
                                    ShowPictureDialog(
                                        data = imageByteArray,
                                        imageDialogVisible,
                                        imageIdx,
                                        productImageCount,
                                        selectedProduct.value!!
                                    )
                                }*/
            }
        }
    }
}