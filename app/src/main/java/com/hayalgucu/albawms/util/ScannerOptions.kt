package com.hayalgucu.albawms.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

data class ScannerOptions(
    val inputString: String,
    val categoryIdx: MutableState<Int> = mutableStateOf(1),
    val searchBoxItems: List<String> = listOf("Default"),
    val searchText: MutableState<String>,
    val dummyText: MutableState<String> = dummyTextForScan,
    val keyboardOptions: Boolean,
    val afterSuffixFunc: () -> Unit
) {

    init {
        scannerOutputFunc()
    }

    private fun scannerOutputFunc() {
        if (categoryIdx.value != 2 && !keyboardOptions) {
            if (!dummyText.value.contains(suffix))
                dummyText.value += inputString

            if (dummyText.value.contains(suffix) && dummyText.value != suffix) {
                searchText.value = dummyText.value.replace(suffix, "")
                dummyText.value = ""

                if (searchText.value != "")
                    afterSuffixFunc()
            } else if (dummyText.value == suffix) {
                dummyText.value = ""
            }
        } else {
            searchText.value = inputString

            if (searchText.value.contains(suffix)) {
                searchText.value = inputString.replace(suffix, "")

                if (searchText.value != "")
                    afterSuffixFunc()
            }
        }
    }
}