package com.v2ray.myvpn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.v2ray.myvpn.navigation.AppNavigation
import com.v2ray.myvpn.repository.ProfileRepository
import java.io.File

class MainActivity : ComponentActivity() {

    private fun writeLog(text: String) {
        try {
            val file =
                File(
                    filesDir,
                    "startup.log"
                )

            file.appendText(
                "${System.currentTimeMillis()} : $text\n"
            )

            Log.d(
                "MYVPN",
                text
            )

        } catch (_: Exception) {
        }
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        writeLog(
            "MainActivity started"
        )

        ProfileRepository.initialize(
            applicationContext
        )

        writeLog(
            "ProfileRepository initialized"
        )

        setContent {
            AppNavigation()
        }

        writeLog(
            "Compose loaded"
        )
    }
}
