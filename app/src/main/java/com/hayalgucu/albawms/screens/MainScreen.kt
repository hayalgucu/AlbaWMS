package com.hayalgucu.albawms.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hayalgucu.albawms.R
import com.hayalgucu.albawms.prefstore.PrefStoreImpl
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.iconSize
import com.hayalgucu.albawms.util.scaffoldPadding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController) {

    val topPadding = 5.dp

    AlbaWMSTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            )
            {
                IconButton(
                    onClick = { navController.navigate("item_info_screen") },
                    modifier = Modifier
                        .width(64.dp)
                        .height(112.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.item_info),
                            contentDescription = "",
                            Modifier.size(
                                iconSize
                            )
                        )
                        Text(
                            text = stringResource(R.string.call_info_item),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                //Konuma gore Stoklar
                IconButton(
                    onClick = { navController.navigate("location_screen") },
                    modifier = Modifier
                        .width(72.dp)
                        //.height(112.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.location_info),
                            contentDescription = "",
                            Modifier.size(
                                iconSize
                            )
                        )
                        Text(
                            text = stringResource(R.string.call_info_location),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                //Tepsi Getir
                IconButton(
                    onClick = { navController.navigate("get_shelf_screen") },
                    modifier = Modifier
                        .width(64.dp)
                        .height(112.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.call_shelf),
                            contentDescription = "",
                            Modifier.size(
                                iconSize
                            )
                        )
                        Text(
                            text = stringResource(R.string.call_tray),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            )
            {
                IconButton(
                    onClick = { navController.navigate("take_item_screen") },
                    modifier = Modifier
                        .width(64.dp)
//                        .height(112.dp)
                ) {
                    Column(
                        //Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add_take_item),
                            contentDescription = "",
                            Modifier.size(
                                iconSize
                            )
                        )
                        Text(
                            text = stringResource(R.string.take_place_item),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                //Ayarlar
                IconButton(
                    onClick = { navController.navigate("settings_screen") },
                    modifier = Modifier
                        .width(64.dp)
                        .height(112.dp)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "",
                            Modifier.size(
                                iconSize
                            )
                        )
                        Text(
                            text = stringResource(R.string.settings),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        var exitAlert by remember { mutableStateOf(false) }
        BackHandler(enabled = true) {
            //Ask For Exit
            exitAlert = true
        }

        val context = LocalContext.current

        if (exitAlert) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = stringResource(R.string.exit)) },
                text = { Text(text = stringResource(R.string.you_will_logout)) },
                confirmButton = {
                    TextButton(onClick = {
                        GlobalScope.launch {
                            PrefStoreImpl(context).saveUser(0)
                        }
                        navController.navigate("login") {
                            popUpTo("main_screen") {
                                inclusive = true
                            }
                        }
                    }) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { exitAlert = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            )
        }
    }
}