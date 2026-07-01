package com.v2ray.myvpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.v2ray.myvpn.navigation.AppNavigation
import com.v2ray.myvpn.repository.ProfileRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        ProfileRepository.initialize(
            applicationContext
        )

        setContent {
            AppNavigation()
        }
    }
}
