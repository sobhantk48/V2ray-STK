package com.v2ray.myvpn.navigation

sealed class Screen(
    val route: String,
    val title: String
) {

    data object Home : Screen(
        route = "home",
        title = "Home"
    )

    data object Profiles : Screen(
        route = "profiles",
        title = "Profiles"
    )

    data object Logs : Screen(
        route = "logs",
        title = "Logs"
    )

    data object Settings : Screen(
        route = "settings",
        title = "Settings"
    )
}
