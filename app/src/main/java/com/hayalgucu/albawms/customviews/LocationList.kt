package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.models.ItemsInLocationModel
import com.hayalgucu.albawms.ui.theme.Primary200

@Composable
fun LocationList(
    itemList: MutableList<ItemsInLocationModel>,
    selectedItemCode: MutableState<String>,
) {
    LazyRow(Modifier.padding(top = 10.dp)) {
        item {
            Column() {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Primary200)
                ) {
                    TableHeaderCell(stringResource(id = R.string.item_code), (LocalConfiguration.current.screenWidthDp * 0.30).dp, onClick = {
                        if (itemList.size > 1) {
                            if (itemList[0].stkKodu >= itemList[1].stkKodu) {
                                itemList.sortBy { it.stkKodu }
                            } else {
                                itemList.sortByDescending { it.stkKodu }
                            }
                        }
                    })
                    TableHeaderCell(stringResource(R.string.item_name), (LocalConfiguration.current.screenWidthDp * 0.40).dp, onClick = {
                        if (itemList.size > 1) {
                            if (itemList[0].stkAdi >= itemList[1].stkAdi) {
                                itemList.sortBy { it.stkAdi }
                            } else {
                                itemList.sortByDescending { it.stkAdi }
                            }
                        }
                    })
                    TableHeaderCell(stringResource(id = R.string.quantity), (LocalConfiguration.current.screenWidthDp * 0.20).dp, onClick = {
                        if (itemList.size > 1) {
                            if (itemList[0].miktar >= itemList[1].miktar) {
                                itemList.sortBy { it.miktar }
                            } else {
                                itemList.sortByDescending { it.miktar }
                            }
                        }
                    })
                }
                LazyColumn() {
                    items(
                        count = itemList.size,
                        key = { index -> itemList[index].stkKodu }) { index ->
                        val item = itemList[index]
                        LocationItemRowItem(
                            itemRowItem = item,
                            selectedItemCode = selectedItemCode,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItemRowItem(
    itemRowItem: ItemsInLocationModel,
    selectedItemCode: MutableState<String>
) {
    Row(
        Modifier
            .background(
                when (selectedItemCode.value) {
                    itemRowItem.stkKodu -> Color.Black
                    else -> Color.Gray
                }
            )
            .selectable(selected = itemRowItem.stkKodu == selectedItemCode.value, onClick = {
                if (selectedItemCode.value == itemRowItem.stkKodu)
                    selectedItemCode.value = ""
                else
                    selectedItemCode.value = itemRowItem.stkKodu
            })
    ) {
        TableCell(text = itemRowItem.stkKodu, (LocalConfiguration.current.screenWidthDp * 0.30).dp)
        TableCell(text = itemRowItem.stkAdi, (LocalConfiguration.current.screenWidthDp * 0.40).dp)
        TableCell(
            text = itemRowItem.miktar.toString(),
            (LocalConfiguration.current.screenWidthDp * 0.20).dp,
            textAlign = TextAlign.Center
        )
    }
}