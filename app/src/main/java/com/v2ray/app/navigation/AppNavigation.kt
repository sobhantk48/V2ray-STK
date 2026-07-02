package com.v2ray.app.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.v2ray.app.security.AdminSession
import com.v2ray.app.ui.about.AboutScreen
import com.v2ray.app.ui.admin.AdminLoginScreen
import com.v2ray.app.ui.admin.AdminScreen
import com.v2ray.app.ui.dashboard.DashboardScreen
import com.v2ray.app.ui.location.LocationListScreen
import com.v2ray.app.ui.settings.LogViewerScreen
import com.v2ray.app.ui.settings.SettingsScreen
import com.v2ray.app.ui.splash.SplashScreen
import com.v2ray.app.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val vm: MainViewModel = viewModel()
    val adminLoggedIn by AdminSession.loggedIn.collectAsState()

    NavHost(nav, AppRoutes.SPLASH) {
        composable(AppRoutes.SPLASH) {
            SplashScreen { nav.navigate(AppRoutes.HOME) { popUpTo(AppRoutes.SPLASH) { inclusive = true } } }
        }
        composable(AppRoutes.HOME) {
            DashboardScreen(
                nav, drawer, vm,
                onAdminClick = {
                    scope.launch { drawer.close() }
                    nav.navigate(if (adminLoggedIn) AppRoutes.ADMIN else AppRoutes.ADMIN_LOGIN)
                },
                onNavigateToSettings = { scope.launch { drawer.close() }; nav.navigate(AppRoutes.SETTINGS) },
                onNavigateToLocations = { scope.launch { drawer.close() }; nav.navigate(AppRoutes.LOCATION_LIST) },
                onNavigateToAbout = { scope.launch { drawer.close() }; nav.navigate(AppRoutes.ABOUT) },
                onNavigateToLogs = { scope.launch { drawer.close() }; nav.navigate(AppRoutes.LOGS) }
            )
        }
        composable(AppRoutes.ADMIN_LOGIN) {
            AdminLoginScreen(
                onSuccess = { nav.navigate(AppRoutes.ADMIN) { popUpTo(AppRoutes.ADMIN_LOGIN) { inclusive = true } } },
                onBack = { nav.popBackStack() }
            )
        }
        composable(AppRoutes.ADMIN) { AdminScreen(vm, onBack = { nav.popBackStack() }) }
        composable(AppRoutes.SETTINGS) { SettingsScreen(vm, onBack = { nav.popBackStack() }) }
        composable(AppRoutes.LOCATION_LIST) { LocationListScreen(vm, onBack = { nav.popBackStack() }) }
        composable(AppRoutes.ABOUT) { AboutScreen(onBack = { nav.popBackStack() }) }
        composable(AppRoutes.LOGS) { LogViewerScreen(onBack = { nav.popBackStack() }) }
    }
}
