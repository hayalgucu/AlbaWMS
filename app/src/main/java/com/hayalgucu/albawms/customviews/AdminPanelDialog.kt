package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.util.suffix
import java.time.LocalDateTime

@Composable
fun AdminPanelDialog(isShow: MutableState<Boolean>, navController: NavController) {
    var adminPasswordTxt by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val time = LocalDateTime.now().minute

    val second = 9 - (time % 10)
    val first = 9 - ((time - (time % 10)) / 10)

    val password = first.toString() + second.toString()

    if (isShow.value) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = stringResource(R.string.admin)) },
            text = {
                Column() {
                    Text(
                        text = stringResource(R.string.enter_admin_password),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp
                    )
                    TextField(
                        modifier = Modifier.padding(top = 15.dp),
                        value = adminPasswordTxt,
                        isError = error,
                        onValueChange = {
                            adminPasswordTxt = it.replace(suffix, "")
                            error = false
                        },
                        label = { Text(text = stringResource(R.string.admin_password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(textAlign = TextAlign.Center)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (password == adminPasswordTxt) {
                        error = false
                        navController.navigate("admin_settings_screen")
                    } else {
                        //Animation
                        error = true
                    }
                }) {
                    Text(text = stringResource(id = R.string.login))
                }
            },
            dismissButton = {
                TextButton(onClick = { isShow.value = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    } else {
        adminPasswordTxt = ""
    }
}