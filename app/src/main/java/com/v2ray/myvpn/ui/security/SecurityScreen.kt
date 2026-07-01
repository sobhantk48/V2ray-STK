package com.v2ray.myvpn.ui.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.viewmodel.AdminViewModel

@Composable
fun SecurityScreen(
    onLogout: () -> Unit = {},
    vm: AdminViewModel = viewModel()
) {
    var password by remember { mutableStateOf("") }
    var adminEnabled by remember { mutableStateOf(true) }
    var exportLocked by remember { mutableStateOf(true) }
    var shareLocked by remember { mutableStateOf(true) }

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
                text = "Security",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = {
                    Text("Admin Password")
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            SecuritySwitch(
                title = "Admin Mode",
                checked = adminEnabled,
                onChange = { adminEnabled = it }
            )

            SecuritySwitch(
                title = "Lock Export",
                checked = exportLocked,
                onChange = { exportLocked = it }
            )

            SecuritySwitch(
                title = "Lock Share",
                checked = shareLocked,
                onChange = { shareLocked = it }
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (password.isNotBlank()) {
                        vm.changePassword(password)
                    }
                }
            ) {
                Text("Save Password")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.logout()
                    onLogout()
                }
            ) {
                Text("Logout Admin")
            }
        }
    }
}

@Composable
private fun SecuritySwitch(
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
