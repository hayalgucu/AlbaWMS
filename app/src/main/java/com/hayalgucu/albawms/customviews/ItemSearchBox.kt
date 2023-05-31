package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme

@ExperimentalComposeUiApi
@Composable
fun ItemSearchBox(
    searchText: MutableState<String>,
    labelList: List<String>,
    labelIdx: MutableState<Int>,
    onKeyboardDone: KeyboardActionScope.() -> Unit,
    onValueChangeListener: (String) -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardController: SoftwareKeyboardController?,
    keyboardOptions: Boolean = false,
    currentFocus: MutableState<String> = mutableStateOf(""),
    fulfillment: Float = 1f,
    isEnabled: MutableState<Boolean> = mutableStateOf(true),
    keyboardType: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    val context = LocalContext.current
    AlbaWMSTheme() {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(fulfillment)
                //.padding(5.dp)
                .focusRequester(focusRequester)
                .onFocusEvent { fs ->
                    currentFocus.value = "stok"
                    if (fs.isFocused && labelList[labelIdx.value] != context.getString(R.string.item_name) && !keyboardOptions) {
                        keyboardController?.hide()
                    }
                },
            value = searchText.value,
            onValueChange = onValueChangeListener,
            label = { Text(labelList[labelIdx.value]) },
            trailingIcon = {
                if (labelList.size > 1) {
                    IconButton(onClick = {
                        searchText.value = ""
                        focusRequester.requestFocus()
                        if (labelIdx.value == labelList.size - 1)
                            labelIdx.value = 0
                        else
                            labelIdx.value += 1
                        if (labelList[labelIdx.value] == context.getString(R.string.item_name))
                            keyboardController?.show()
                        else
                            keyboardController?.hide()
                    }, enabled = isEnabled.value) {
                        CustomIcon(
                            icon = Icons.Rounded.ChangeCircle,
                            cd = "Arama Parametresi degitir.",
                            size = 32.dp,
                            enabled = isEnabled.value
                        )
                    }
                }
            },
            keyboardActions = KeyboardActions(onDone = onKeyboardDone),
            singleLine = true,
            keyboardOptions = keyboardType,
            enabled = isEnabled.value
        )
    }
}