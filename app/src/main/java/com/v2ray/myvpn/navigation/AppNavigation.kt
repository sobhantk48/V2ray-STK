package com.v2ray.myvpn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val adminLoggedIn by
        AdminSession
            .loggedIn
            .collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {

        composable(
         AppRoutes.SPLASH
    ) {
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
        composable(
            AppRoutes.HOME
        ) {
            HomeScreen()
        }

        composable(
            AppRoutes.ADMIN_LOGIN
        ) {

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

        composable(
            AppRoutes.DASHBOARD
        ) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                DashboardScreen()
            }
        }

        composable(
            AppRoutes.PROFILES
        ) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                ProfilesScreen()
            }
        }

        composable(
            AppRoutes.EDIT_PROFILE
        ) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                EditProfileScreen()
            }
        }

        composable(
            AppRoutes.SUBSCRIPTION
        ) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                SubscriptionScreen()
            }
        }

        composable(
            AppRoutes.LOGS
        ) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                LogsScreen()
            }
        }

        composable(
            AppRoutes.SECURITY
        ) {

            AdminProtected(
                navController,
                adminLoggedIn
            ) {

                SecurityScreen(

                    onLogout = {

                        navController.navigate(
                            AppRoutes.HOME
                        ) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }

        composable(
            AppRoutes.SETTINGS
        ) {

            SettingsScreen()
        }

        composable(
            AppRoutes.ABOUT
        ) {

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

    LaunchedEffect(
        loggedIn
    ) {

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
