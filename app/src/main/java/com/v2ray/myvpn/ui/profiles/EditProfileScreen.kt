package com.v2ray.myvpn.ui.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.Profile
import com.v2ray.myvpn.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profile: Profile? = null,
    onSaved: () -> Unit = {},
    onCancel: () -> Unit = {},
    vm: ProfileViewModel = viewModel()
) {

    var name by remember { mutableStateOf(profile?.name ?: "") }
    var protocol by remember { mutableStateOf(profile?.protocol ?: "vless") }
    var host by remember { mutableStateOf(profile?.host ?: "") }
    var port by remember { mutableStateOf(profile?.port?.toString() ?: "443") }
    var uuid by remember { mutableStateOf(profile?.uuid ?: "") }
    var security by remember { mutableStateOf(profile?.security ?: "tls") }
    var network by remember { mutableStateOf(profile?.network ?: "tcp") }
    var sni by remember { mutableStateOf(profile?.sni ?: "") }
    var path by remember { mutableStateOf(profile?.path ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (profile == null) "Add Profile" else "Edit Profile")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Field("Name", name) { name = it }
            Field("Protocol", protocol) { protocol = it }
            Field("Host", host) { host = it }
            Field("Port", port) { port = it }
            Field("UUID", uuid) { uuid = it }
            Field("Security", security) { security = it }
            Field("Network", network) { network = it }
            Field("SNI", sni) { sni = it }
            Field("Path", path) { path = it }

            Spacer(Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (profile == null) {
                        vm.addProfile(
                            name, protocol, host,
                            port.toIntOrNull() ?: 443,
                            uuid, security, network, sni, path
                        )
                    } else {
                        vm.updateProfile(
                            profile.copy(
                                name = name,
                                protocol = protocol,
                                host = host,
                                port = port.toIntOrNull() ?: 443,
                                uuid = uuid,
                                security = security,
                                network = network,
                                sni = sni,
                                path = path
                            )
                        )
                    }

                    onSaved()
                }
            ) {
                Text("Save")
            }

            Spacer(Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
private fun Field(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onChange,
        label = { Text(label) }
    )

    Spacer(Modifier.height(12.dp))
}
