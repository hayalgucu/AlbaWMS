package com.hayalgucu.albawms.screens

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.CustomIcon
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.scaffoldPadding
import com.hayalgucu.albawms.util.suffix
import com.hayalgucu.albawms.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    BackHandler(enabled = true) {

    }

    var username by remember { viewModel.username }
    var password by remember { mutableStateOf("") }
    var fieldsEmpty by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }

    val loginSucceed by remember { viewModel.loginSucceed }
    val errorMessage by remember { viewModel.errorMessage }
    var isLoading by remember { viewModel.isLoading }

    LaunchedEffect(loginSucceed) {
        if (loginSucceed) {
            navController.navigate("main_screen")
        }
    }

    AlbaWMSTheme {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding))
        {
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
                    title = { Text(stringResource(R.string.error)) },
                    text = { Text(errorMessage) }, //"Kullanici adi veya sifre hatali."
                    confirmButton = {
                        TextButton(onClick = {
                            isLoading = false
                            viewModel.errorMessage.value = ""
                        }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                )
            }
            if (fieldsEmpty) {
                AlertDialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    ),
                    title = { Text(stringResource(id = R.string.error)) },
                    text = { Text(stringResource(R.string.username_password_cannotbe_blank)) },
                    confirmButton = {
                        TextButton(onClick = {
                            fieldsEmpty = false
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    }
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it.replace(suffix, "") },
                    label = {
                        Text(text = stringResource(R.string.username))
                    }, modifier = Modifier.padding(top = 25.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.replace(suffix, "") },
                    label = { Text(text = stringResource(R.string.password)) },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisibility)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Icon(imageVector = image, "Password Visibility")
                        }
                    },
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        fieldsEmpty = username.isEmpty() || password.isEmpty()

                        if (!fieldsEmpty) {
                            viewModel.login(username, password)
                        }
                    })
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(onClick = {
                    fieldsEmpty = username.isEmpty() || password.isEmpty()

                    if (!fieldsEmpty) {
                        viewModel.login(username, password)
                    }

                }) {
                    Text(text = stringResource(R.string.login))
                }

                Spacer(modifier = Modifier.height(10.dp))

                val app = LocalContext.current as Activity

                val manager = app.packageManager
                val version = manager.getPackageInfo(
                    app.packageName,
                    PackageManager.GET_ACTIVITIES
                ).versionName
                Text(stringResource(id = R.string.version_params, version))

                Spacer(modifier = Modifier.height(25.dp))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(15.dp)
            ) {
                IconButton(onClick = {
                    viewModel.isLoading.value = true
                    viewModel.getUpdateInfo()
                }) {
                    CustomIcon(icon = Icons.Rounded.Refresh, cd = "Update")
                }
                IconButton(onClick = {
                    navController.navigate("settings_screen")
                }) {
                    CustomIcon(icon = Icons.Rounded.Settings, cd = "Settings")
                }
            }
        }
    }
}