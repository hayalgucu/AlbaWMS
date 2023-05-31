package com.hayalgucu.albawms.customviews

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Dialog
import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme

@Composable
fun MultipleLocationDialog(
    itemLocationList: MutableState<List<ItemLocationModel>>,
    isVisible: MutableState<Boolean>
) {
    AlbaWMSTheme() {
        Dialog(onDismissRequest = {
            itemLocationList.value = listOf()
            isVisible.value = false
        }) {
            Surface() {
                ItemLocationList(itemLocationModelList = itemLocationList.value)
            }
        }
    }
}