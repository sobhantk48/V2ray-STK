package com.v2ray.myvpn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.v2ray.myvpn.security.AdminSession
import com.v2ray.myvpn.ui.about.AboutScreen
import com.v2ray.myvpn.ui.admin.AdminLoginScreen
import com.v2ray.myvpn.ui.admin.DashboardScreen
import com.v2ray.myvpn.ui.home.HomeScreen
import com.v2ray.myvpn.ui.logs.LogsScreen
import com.v2ray.myvpn.ui.profiles.EditProfileScreen
import com.v2ray.myvpn.ui.profiles.ProfilesScreen
import com.v2ray.myvpn.ui.security.SecurityScreen
import com.v2ray.myvpn.ui.settings.SettingsScreen
import com.v2ray.myvpn.ui.splash.SplashScreen
import com.v2ray.myvpn.ui.subscription.SubscriptionScreen
import com.v2ray.myvpn.viewmodel.ProfileViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // فقط یک ProfileViewModel برای کل برنامه
    val profileViewModel: ProfileViewModel = viewModel()

    val adminLoggedIn by
        AdminSession.loggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {

        composable(AppRoutes.SPLASH) {

            SplashScreen(
                onFinish = {

                    navController.navigate(
                        AppRoutes.HOME
                    ) {
                        popUpTo(
                            AppRoutes.SPLASH
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppRoutes.HOME) {

            HomeScreen(

                onConnect = {
                },

                onAdmin = {

                    navController.navigate(
                        AppRoutes.ADMIN_LOGIN
                    )
                }
            )
        }

        composable(AppRoutes.ADMIN_LOGIN) {

            AdminLoginScreen(

                onSuccess = {

                    navController.navigate(
                        AppRoutes.DASHBOARD
                    ) {

                        popUpTo(
                            AppRoutes.ADMIN_LOGIN
                        ) {
                            inclusive = true
                        }
                    }
                },

                onBack = {

                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.DASHBOARD) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                DashboardScreen(

                    onProfiles = {

                        navController.navigate(
                            AppRoutes.PROFILES
                        )
                    },

                    onSubscription = {

                        navController.navigate(
                            AppRoutes.SUBSCRIPTION
                        )
                    },

                    onLogs = {

                        navController.navigate(
                            AppRoutes.LOGS
                        )
                    },

                    onSettings = {

                        navController.navigate(
                            AppRoutes.SETTINGS
                        )
                    },

                    onSecurity = {

                        navController.navigate(
                            AppRoutes.SECURITY
                        )
                    },

                    onAbout = {

                        navController.navigate(
                            AppRoutes.ABOUT
                        )
                    },

                    onLogout = {

                        AdminSession.logout()

                        navController.navigate(
                            AppRoutes.HOME
                        ) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }

        composable(AppRoutes.PROFILES) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                ProfilesScreen(

                    vm = profileViewModel,

                    onAdd = {

                        navController.navigate(
                            AppRoutes.EDIT_PROFILE
                        )
                    },

                    onEdit = { _ ->

                        navController.navigate(
                            AppRoutes.EDIT_PROFILE
                        )
                    }
                )
            }
        }

        composable(AppRoutes.EDIT_PROFILE) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                EditProfileScreen(

                    vm = profileViewModel,

                    onSaved = {

                        navController.popBackStack()
                    },

                    onCancel = {

                        navController.popBackStack()
                    }
                )
            }
        }

        composable(AppRoutes.SUBSCRIPTION) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                SubscriptionScreen()
            }
        }

        composable(AppRoutes.LOGS) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                LogsScreen()
            }
        }

        composable(AppRoutes.SECURITY) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                SecurityScreen(

                    onLogout = {

                        AdminSession.logout()

                        navController.navigate(
                            AppRoutes.HOME
                        ) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }

        composable(AppRoutes.SETTINGS) {

            SettingsScreen()
        }

        composable(AppRoutes.ABOUT) {

            AboutScreen()
        }
    }
}

@Composable
private fun AdminProtected(
    navController: NavHostController,
    loggedIn: Boolean,
    content: @Composable () -> Unit
) {

    LaunchedEffect(loggedIn) {

        if (!loggedIn) {

            navController.navigate(
                AppRoutes.ADMIN_LOGIN
            )
        }
    }

    if (loggedIn) {
        content()
    }
}
