package com.hayalgucu.albawms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hayalgucu.albawms.screens.AdminSettingsScreen
import com.hayalgucu.albawms.screens.GetShelfScreen
import com.hayalgucu.albawms.screens.ItemInfoScreen
import com.hayalgucu.albawms.screens.LocationScreen
import com.hayalgucu.albawms.screens.LoginScreen
import com.hayalgucu.albawms.screens.MainScreen
import com.hayalgucu.albawms.screens.SettingsScreen
import com.hayalgucu.albawms.screens.AddRemoveItemScreen
import com.hayalgucu.albawms.ui.theme.AlbaWMSTheme
import com.hayalgucu.albawms.util.pageText
import com.hayalgucu.albawms.util.scaffoldPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AlbaWMSTheme {

                Scaffold(topBar = {
                    TopAppBar {
                        Row(
                            modifier = Modifier.padding(5.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Image(
                                    painter = painterResource(id = R.drawable.alba_top),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(height = 90.dp, width = 240.dp)
                                )
                            }
                            Row(
                                Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(pageText.value, fontSize = 14.sp)
                            }
                        }
                    }
                }) {
                    scaffoldPadding = it
                    NavigationForMainPages(navController = navController)
                }

            }
        }
    }
}

@Composable
fun NavigationForMainPages(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("settings_screen") {
            SettingsScreen(navController = navController)
        }
        composable("admin_settings_screen") {
            AdminSettingsScreen(navController = navController)
        }
        composable("item_info_screen") {
            ItemInfoScreen()
        }
        composable("location_screen") {
            LocationScreen()
        }
        composable("get_shelf_screen") {
            GetShelfScreen()
        }
        composable("take_item_screen") {
            AddRemoveItemScreen()
        }
    }
}
