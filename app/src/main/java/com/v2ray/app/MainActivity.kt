package com.v2ray.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.v2ray.app.navigation.AppNavigation
import com.v2ray.app.ui.theme.V2rayAppTheme
import com.v2ray.app.utils.Logger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.initialize(this)
        Logger.writeLog("MainActivity started")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val perms = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (perms.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, perms.toTypedArray(), 100)
            }
        }

        setContent {
            V2rayAppTheme {
                AppNavigation()
            }
        }
        Logger.writeLog("Compose loaded")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            Logger.writeLog("Storage permissions granted")
            Toast.makeText(this, "Storage permissions granted", Toast.LENGTH_SHORT).show()
        }
    }
}
