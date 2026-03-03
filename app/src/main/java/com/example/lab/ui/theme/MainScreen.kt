package com.example.lab.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import android.content.res.Configuration
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {

            if (!isLandscape) {
                NavigationBar(containerColor = Color.Black) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Star, contentDescription = "Calc") },
                        label = { Text("Калькулятор") },
                        selected = currentRoute == "calculator",
                        onClick = {
                            navController.navigate("calculator") {

                                popUpTo("calculator") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ApplePurple,
                            selectedTextColor = ApplePurple,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = AppleDarkGray
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Map") },
                        label = { Text("API") },
                        selected = currentRoute == "location",
                        onClick = {
                            navController.navigate("location") {
                                popUpTo("calculator") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ApplePurple,
                            selectedTextColor = ApplePurple,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = AppleDarkGray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "calculator",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("calculator") {
                CalculatorScreen()
            }
            composable("location") {
                LocationScreen()
            }
        }
    }
}