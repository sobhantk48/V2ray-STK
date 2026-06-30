package com.v2ray.myvpn.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNav(
    navController: NavHostController,
    currentRoute: String?
) {

    val items =
        listOf(
            Screen.Home,
            Screen.Profiles,
            Screen.Logs,
            Screen.Settings
        )

    NavigationBar {

        items.forEach { screen ->

            NavigationBarItem(
                selected =
                    currentRoute ==
                        screen.route,

                onClick = {

                    navController.navigate(
                        screen.route
                    ) {
                        launchSingleTop =
                            true
                    }
                },

                label = {
                    Text(
                        screen.title
                    )
                },

                icon = {}
            )
        }
    }
}
