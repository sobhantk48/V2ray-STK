package com.v2ray.myvpn.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onSave: () -> Unit = {}
) {

    var autoConnect by remember {
        mutableStateOf(false)
    }

    var startOnBoot by remember {
        mutableStateOf(false)
    }

    var autoReconnect by remember {
        mutableStateOf(true)
    }

    var batteryOptimization by remember {
        mutableStateOf(false)
    }

    var keepAlive by remember {
        mutableStateOf(true)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            SettingItem(
                title = "Auto Connect",
                checked = autoConnect,
                onChange = {
                    autoConnect = it
                }
            )

            SettingItem(
                title = "Start On Boot",
                checked = startOnBoot,
                onChange = {
                    startOnBoot = it
                }
            )

            SettingItem(
                title = "Auto Reconnect",
                checked = autoReconnect,
                onChange = {
                    autoReconnect = it
                }
            )

            SettingItem(
                title = "Battery Optimization",
                checked = batteryOptimization,
                onChange = {
                    batteryOptimization = it
                }
            )

            SettingItem(
                title = "Keep Alive",
                checked = keepAlive,
                onChange = {
                    keepAlive = it
                }
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSave
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onChange
        )
    }
}
