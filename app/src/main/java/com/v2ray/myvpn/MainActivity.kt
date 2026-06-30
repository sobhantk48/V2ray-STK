package com.v2ray.myvpn

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.v2ray.myvpn.navigation.BottomNav
import com.v2ray.myvpn.navigation.Screen
import com.v2ray.myvpn.ui.screen.HomeScreen
import com.v2ray.myvpn.ui.screen.LogsScreen
import com.v2ray.myvpn.ui.screen.ProfilesScreen
import com.v2ray.myvpn.ui.screen.SettingsScreen
import com.v2ray.myvpn.vpn.MyVpnService

class MainActivity : ComponentActivity() {

    private val vpnPermission =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            startService(
                Intent(
                    this,
                    MyVpnService::class.java
                )
            )
        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        setContent {

            val navController =
                rememberNavController()

            val backStack by
                navController
                    .currentBackStackEntryAsState()

            Surface(
                modifier =
                    Modifier.fillMaxSize()
            ) {

                Scaffold(

                    bottomBar = {
                        BottomNav(
                            navController,
                            backStack
                                ?.destination
                                ?.route
                        )
                    }

                ) { padding ->

                    NavHost(
                        navController =
                            navController,

                        startDestination =
                            Screen.Home.route,

                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding)
                    ) {

                        composable(
                            Screen.Home.route
                        ) {

                            HomeScreen(

                                onConnect = {

                                    val intent =
                                        VpnService
                                            .prepare(
                                                this@MainActivity
                                            )

                                    if (
                                        intent != null
                                    ) {

                                        vpnPermission
                                            .launch(
                                                intent
                                            )

                                    } else {

                                        startService(
                                            Intent(
                                                this@MainActivity,
                                                MyVpnService::class.java
                                            )
                                        )
                                    }
                                }
                            )
                        }

                        composable(
                            Screen.Profiles.route
                        ) {
                            ProfilesScreen()
                        }

                        composable(
                            Screen.Logs.route
                        ) {
                            LogsScreen()
                        }

                        composable(
                            Screen.Settings.route
                        ) {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}
