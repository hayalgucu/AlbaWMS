package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.ui.theme.Primary200

@Composable
fun ItemLocationList(
    itemLocationModelList: List<ItemLocationModel>,
    selectedLocation: MutableState<ItemLocationModel?> = mutableStateOf(null),
    isClickable: Boolean = false
) {
    Column(Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Primary200)
        ) {
            TableHeaderCell(text = stringResource(R.string.machine), (LocalConfiguration.current.screenWidthDp * 0.20).dp)
            TableHeaderCell(text = stringResource(R.string.tray), (LocalConfiguration.current.screenWidthDp * 0.15).dp)
            TableHeaderCell(text = stringResource(id = R.string.location), (LocalConfiguration.current.screenWidthDp * 0.40).dp)
            TableHeaderCell(text = stringResource(id = R.string.quantity), (LocalConfiguration.current.screenWidthDp * 0.20).dp)
        }
        LazyColumn() {
            items(itemLocationModelList) { rowItem ->
                ItemLocationRowItem(itemLocationModel = rowItem, selectedLocation, isClickable)
            }
        }
    }
}

@Composable
fun ItemLocationRowItem(
    itemLocationModel: ItemLocationModel,
    selectedLocation: MutableState<ItemLocationModel?>,
    isClickable: Boolean
) {
    Row(
        modifier = Modifier
            .background(
                if (selectedLocation.value == itemLocationModel)
                    Color.Black
                else
                    Color.Gray
            )
            .selectable(selectedLocation.value == itemLocationModel, onClick = {
                if (isClickable) {
                    if (selectedLocation.value == null)
                        selectedLocation.value = itemLocationModel
                    else if (selectedLocation.value != itemLocationModel)
                        selectedLocation.value = itemLocationModel
                    else
                        selectedLocation.value = null
                }
            })
    ) {
        TableCell(
            text = itemLocationModel.hcrMakineNo.toString(),
            (LocalConfiguration.current.screenWidthDp * 0.20).dp
        )
        TableCell(
            text = itemLocationModel.hcrKonumNo.toString(),
            (LocalConfiguration.current.screenWidthDp * 0.15).dp
        )
        TableCell(
            text = itemLocationModel.altkonum,
            (LocalConfiguration.current.screenWidthDp * 0.40).dp
        )
        TableCell(
            text = itemLocationModel.sipKalan.toString(),
            (LocalConfiguration.current.screenWidthDp * 0.20).dp, textAlign = TextAlign.Center
        )
    }
}