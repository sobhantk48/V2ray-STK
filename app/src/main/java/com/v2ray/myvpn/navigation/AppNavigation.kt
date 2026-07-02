package com.v2ray.myvpn.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.v2ray.myvpn.security.AdminSession
import com.v2ray.myvpn.ui.about.AboutScreen
import com.v2ray.myvpn.ui.admin.AdminLoginScreen
import com.v2ray.myvpn.ui.admin.AdminScreen
import com.v2ray.myvpn.ui.dashboard.DashboardScreen
import com.v2ray.myvpn.ui.location.LocationListScreen
import com.v2ray.myvpn.ui.settings.SettingsScreen
import com.v2ray.myvpn.ui.splash.SplashScreen
import com.v2ray.myvpn.viewmodel.MainViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel()

    val adminLoggedIn by AdminSession.loggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onFinish = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.HOME) {
            DashboardScreen(
                navController = navController,
                drawerState = drawerState,
                viewModel = mainViewModel,
                onAdminClick = {
                    if (adminLoggedIn) {
                        navController.navigate(AppRoutes.ADMIN)
                    } else {
                        navController.navigate(AppRoutes.ADMIN_LOGIN)
                    }
                }
            )
        }

        composable(AppRoutes.ADMIN_LOGIN) {
            AdminLoginScreen(
                onSuccess = {
                    navController.navigate(AppRoutes.ADMIN) {
                        popUpTo(AppRoutes.ADMIN_LOGIN) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ADMIN) {
            AdminScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.SETTINGS) {
            SettingsScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.LOCATION_LIST) {
            LocationListScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
