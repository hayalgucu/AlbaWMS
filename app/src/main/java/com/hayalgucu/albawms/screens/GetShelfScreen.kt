package com.hayalgucu.albawms.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.CustomIcon
import com.hayalgucu.albawms.customviews.ItemSearchBox
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.ScannerOptions
import com.hayalgucu.albawms.util.machineList
import com.hayalgucu.albawms.util.scaffoldPadding
import com.hayalgucu.albawms.viewmodels.GetShelfViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun GetShelfScreen(
    viewModel: GetShelfViewModel = hiltViewModel()
) {
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    val shelfNo = remember { viewModel.shelfNo }

    var selectedMachine by remember { mutableStateOf(machineList.first()) }
    var machineExpanded by remember { mutableStateOf(false) }

    val errorMessage by remember { viewModel.errorMessage }
    var isLoading by remember { viewModel.isLoading }

    AlbaWMSTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 5.dp)
        ) {

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.machine_no))
                ExposedDropdownMenuBox(
                    expanded = machineExpanded,
                    onExpandedChange = { machineExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedMachine.name, fontSize = 26.sp)
                    ExposedDropdownMenu(
                        expanded = machineExpanded,
                        onDismissRequest = { machineExpanded = false }) {
                        repeat(machineList.size) {
                            DropdownMenuItem(onClick = {
                                selectedMachine = machineList[it]
                                machineExpanded = false
                            }) {
                                Text(text = machineList[it].name, fontSize = 24.sp)
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                val searchBoxItems = listOf(stringResource(id = R.string.tray))
                val categoryIdx = remember { mutableStateOf(0) }
                ItemSearchBox(
                    fulfillment = .6f,
                    searchText = shelfNo,
                    labelList = searchBoxItems,
                    labelIdx = categoryIdx,
                    onKeyboardDone = {
                        if (shelfNo.value != "") {
                            //Search
                            viewModel.getShelf(selectedMachine.no)
                        }
                    },
                    onValueChangeListener = {
                        ScannerOptions(
                            inputString = it,
                            categoryIdx = categoryIdx,
                            searchBoxItems = searchBoxItems,
                            searchText = shelfNo,
                            keyboardOptions = true,
                            afterSuffixFunc = {
                                if (shelfNo.value != "") {
                                    //Search
                                    viewModel.getShelf(selectedMachine.no)
                                }
                            }
                        )
                    },
                    focusRequester = focusRequester,
                    keyboardController = keyboardController,
                    keyboardOptions = true,
                    keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                IconButton(modifier = Modifier.weight(.2f), onClick = {
                    //Search
                    if (shelfNo.value != "") {
                        viewModel.getShelf(selectedMachine.no)
                    }
                }) {
                    CustomIcon(icon = Icons.Rounded.Search, cd = "Arama")
                }

                Button(modifier = Modifier.weight(.2f), onClick = {
                    viewModel.parkShelf(selectedMachine.no)
                }) {
                    Text(text = stringResource(R.string.park))
                }
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isLoading) {
                    Dialog(
                        onDismissRequest = {}
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            if (errorMessage.isNotEmpty()) {
                AlertDialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    ),
                    title = { Text(stringResource(id = R.string.error)) },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        TextButton(onClick = {
                            isLoading = false
                            viewModel.errorMessage.value = ""
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    }
                )
            }
        }
    }
}