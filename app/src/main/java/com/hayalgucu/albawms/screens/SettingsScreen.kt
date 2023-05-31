package com.hayalgucu.albawms.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.customviews.AdminPanelDialog
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.scaffoldPadding
import com.hayalgucu.albawms.util.terminatorNames
import com.hayalgucu.albawms.util.terminatorValues
import com.hayalgucu.albawms.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    var terminatorExpanded by remember { mutableStateOf(false) }
    var selectedTerminator by remember { viewModel.selectedTerminator }

    val adminDialogShow = remember { mutableStateOf(false) }

    AlbaWMSTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        terminatorExpanded = true
                    }, horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                Text(text = stringResource(R.string.reader_type), fontSize = 24.sp)
                Box(Modifier.padding(end = 20.dp)) {
                    Text(selectedTerminator, fontSize = 24.sp)
                    DropdownMenu(
                        expanded = terminatorExpanded,
                        onDismissRequest = { terminatorExpanded = false }) {
                        repeat(terminatorNames.size) {
                            DropdownMenuItem(onClick = {
                                selectedTerminator = terminatorNames[it]
                                viewModel.saveTerminator(terminatorValues[it])
                                terminatorExpanded = false
                            }) {
                                Text(text = terminatorNames[it], fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    adminDialogShow.value = true
                }) {
                    Text(text = stringResource(R.string.admin_settings))
                }
            }
            AdminPanelDialog(isShow = adminDialogShow, navController = navController)
        }
    }
}