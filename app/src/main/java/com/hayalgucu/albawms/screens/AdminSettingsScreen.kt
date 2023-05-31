package com.hayalgucu.albawms.screens

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.CustomIcon
import com.hayalgucu.albawms.prefstore.PrefStoreImpl
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.inactivityTime
import com.hayalgucu.albawms.util.startDateMinus
import com.hayalgucu.albawms.util.suffix
import com.hayalgucu.albawms.viewmodels.SettingsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

@Composable
fun AdminSettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    var inactiveTime by remember { inactivityTime }
    var startDateMinus by remember { startDateMinus }

    var shutdownError by remember { mutableStateOf(false) }
    var shouldShutdown by remember {
        mutableStateOf(false)
    }

    val activity = (LocalContext.current as? Activity)

    BackHandler(enabled = true) {
        //Ask For Exit
        if (shouldShutdown)
            shutdownError = true
        else
            navController.popBackStack()
    }

    val systemUiController = rememberSystemUiController()

    var keyboardOptions by remember { viewModel.keyboardOptions }
    var inactivityText by remember { mutableStateOf(inactiveTime.toString()) }
    var startDateText by remember { mutableStateOf(startDateMinus.toString()) }


    var firmExpanded by remember { mutableStateOf(false) }

    var baseAddress by remember { viewModel.baseUrl }

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    AlbaWMSTheme {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (shutdownError) {
                AlertDialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    ),
                    title = { Text(stringResource(id = R.string.warning)) },
                    text = { Text(stringResource(R.string.will_shutdown_for_service)) },
                    confirmButton = {
                        TextButton(onClick = {
                            //Shutdown
                            activity?.finish()
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    }
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(.6f),
                    value = baseAddress,
                    onValueChange = { baseAddress = it.replace(suffix, "") },
                    label = { Text(text = stringResource(R.string.server_address)) },
                    placeholder = { if (baseAddress.isEmpty()) Text(stringResource(R.string.server_example)) })

                IconButton(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.CenterVertically),
                    onClick = {
                        viewModel.setBaseUrl(baseAddress)
                        Toast.makeText(
                            context,
                            context.getString(R.string.url_saved),
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    CustomIcon(icon = Icons.Rounded.Done, cd = "Onayla")
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(.6f),
                    value = inactivityText,
                    onValueChange = { inactivityText = it.replace(suffix, "") },
                    label = { Text(stringResource(R.string.inactivity)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                IconButton(modifier = Modifier
                    .padding(start = 15.dp)
                    .align(Alignment.CenterVertically), onClick = {
                    if (inactivityText.isNotEmpty()) {
                        inactiveTime = inactivityText.toLong()
                        viewModel.saveInactivity(inactiveTime)
                    }
                }) {
                    CustomIcon(icon = Icons.Rounded.Done, cd = "Kaydet")
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.allow_keyboards), fontSize = 18.sp,
                    modifier = Modifier.padding(end = 15.dp)
                )
                Checkbox(checked = keyboardOptions, onCheckedChange = {
                    keyboardOptions = it
                    viewModel.saveKeyboardOption(it)
                })
            }

/*            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(.6f),
                    value = startDateText,
                    onValueChange = { startDateText = it.replace(suffix, "") },
                    label = { Text(stringResource(R.string.start_date)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                IconButton(modifier = Modifier
                    .padding(start = 15.dp)
                    .align(Alignment.CenterVertically), onClick = {
                    if (startDateText.isNotEmpty()) {
                        startDateMinus = startDateText.toLong()
                        viewModel.saveStartDate(startDateMinus)
                    }
                }) {
                    CustomIcon(icon = Icons.Rounded.Done, cd = "Kaydet")
                }
            }*/

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    if (inactivityText.isNotEmpty()) {
                        viewModel.saveAllSettings(inactivityText)
                        Toast.makeText(
                            context,
                            context.getString(R.string.settings_saved),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.settings_cant_saved),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }) {
                    Text(stringResource(R.string.save_settings))
                }
            }
        }
    }
}