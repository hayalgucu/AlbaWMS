package com.hayalgucu.albawms.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.CustomIcon
import com.hayalgucu.albawms.customviews.ItemSearchBox
import com.hayalgucu.albawms.customviews.LocationList
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.ScannerOptions
import com.hayalgucu.albawms.viewmodels.LocationViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel = hiltViewModel()
) {

    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchText = remember { mutableStateOf("") }
    val selectedItemCode = remember { mutableStateOf("") }

    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    var deleteAlert by remember { mutableStateOf(false) }

    val isLocationDialogVisible = remember { mutableStateOf(false) }
//    val selectedProductLocationList = remember { productViewModel.productLocationList }

    AlbaWMSTheme {

        SideEffect {
            if (searchText.value.isBlank()) {
                try {
                    focusRequester.requestFocus()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (searchText.value.isNotBlank() && viewModel.locationItemList.isNotEmpty()) {
                searchText.value = ""
            }
        }

        if (errorMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ),
                title = { Text(stringResource(R.string.warning)) },
                text = {
                    when {
                        errorMessage.contains("Empty") -> Text(stringResource(R.string.no_item_in_location))
                        errorMessage.contains("sıfır") -> Text(text = stringResource(R.string.item_quant_not_zero))
                        else -> Text(errorMessage)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (!errorMessage.contains("sıfır"))
                            searchText.value = ""
                        viewModel.errorMessage.value = ""
                        selectedItemCode.value = ""
                    }) {
                        Text(stringResource(id = R.string.ok))
                    }
                }
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {

            if (isLoading) {
                Dialog(
                    onDismissRequest = {}
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                val searchBoxItems = listOf(stringResource(id = R.string.location))
                val categoryIdx = remember { mutableStateOf(0) }
                ItemSearchBox(
                    fulfillment = .6f,
                    searchText = searchText,
                    labelList = searchBoxItems,
                    labelIdx = categoryIdx,
                    onKeyboardDone = {
                        if (searchText.value != "") {
                            //Search
                            viewModel.getLocationItems(searchText.value)
                        }
                    },
                    onValueChangeListener = {
                        ScannerOptions(
                            inputString = it,
                            categoryIdx = categoryIdx,
                            searchBoxItems = searchBoxItems,
                            searchText = searchText,
                            keyboardOptions = true,
                            afterSuffixFunc = {
                                if (searchText.value != "") {
                                    //Search
                                    viewModel.getLocationItems(searchText.value)
                                }
                            }
                        )
                    },
                    focusRequester = focusRequester,
                    keyboardController = keyboardController,
                    keyboardOptions = true
                )

                IconButton(modifier = Modifier.weight(.2f), onClick = {
                    //Search
                    if (searchText.value != "") {
                        selectedItemCode.value = ""
                        viewModel.getLocationItems(searchText.value)
                    }
                }) {
                    CustomIcon(icon = Icons.Rounded.Search, cd = "Arama")
                }

                IconButton(modifier = Modifier.weight(.2f), onClick = {
                    //Call Tray
                    viewModel.callShelf()
                }, enabled = viewModel.selectedLocation.value != "") {
                    CustomIcon(
                        icon = Icons.Rounded.Unarchive,
                        cd = "Call Tray",
                        enabled = viewModel.selectedLocation.value != ""
                    )
                }
            }

            if (viewModel.locationItemList.isNotEmpty()) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(id = R.string.location_params, viewModel.selectedLocation.value), fontSize = 16.sp)
                }
            }

            /*            if (isLocationDialogVisible.value) {
                            MultipleLocationDialog(
                                itemLocationList = selectedProductLocationList,
                                isVisible = isLocationDialogVisible
                            )
                        }*/

            //List
            if (viewModel.locationItemList.isNotEmpty()) {
                LocationList(
                    itemList = viewModel.locationItemList,
                    selectedItemCode = selectedItemCode
                )
            }
        }
    }
}