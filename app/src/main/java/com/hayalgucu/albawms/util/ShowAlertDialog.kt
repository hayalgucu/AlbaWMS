package com.hayalgucu.albawms.util

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme

@Composable
fun ShowAlertDialog(
    title: String = stringResource(id = R.string.error),
    text: String,
    confBtnText: String = stringResource(id = R.string.ok),
    errorState: MutableState<String>
) {
    AlbaWMSTheme {
        AlertDialog(onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton(onClick = {
                    errorState.value = ""
                }) {
                    Text(confBtnText)
                }
            }
        )
    }
}