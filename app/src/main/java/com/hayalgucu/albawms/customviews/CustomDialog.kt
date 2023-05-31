package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.models.ItemModel
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.largeWidth
import com.hayalgucu.albawms.util.midWidth

@Composable
fun CustomDialog(
    itemList: List<ItemModel>,
    selectedItem: MutableState<ItemModel?>,
    searchText: MutableState<String>
) {
    var dialogVisible by remember { mutableStateOf(true) }

    if (dialogVisible) {
        AlbaWMSTheme {
            Dialog(onDismissRequest = {
                dialogVisible = false
                searchText.value = ""
            }) {
                Surface() {
                    Column(Modifier.padding(5.dp)) {
                        Row {
                            TableHeaderCell(text = stringResource(id = R.string.item_code), midWidth)
                            TableHeaderCell(text = stringResource(id = R.string.item_name), largeWidth)
                        }
                        LazyColumn() {
                            items(itemList) { rowItem ->
                                Row(
                                    Modifier
                                        //.fillMaxWidth()
                                        .clickable {
                                            selectedItem.value = rowItem
                                            searchText.value = rowItem.stkKodu
                                            dialogVisible = false
                                        }) {
                                    TableCell(text = rowItem.stkKodu, midWidth)
                                    TableCell(text = rowItem.stkAdi, largeWidth)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}