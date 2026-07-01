package com.v2ray.myvpn.ui.profiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.Profile
import com.v2ray.myvpn.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    profile: Profile? = null,
    onSaved: () -> Unit = {},
    onCancel: () -> Unit = {},
    vm: ProfileViewModel = viewModel()
) {

    var name by remember {
        mutableStateOf(
            profile?.name ?: ""
        )
    }

    var protocol by remember {
        mutableStateOf(
            profile?.protocol ?: "vless"
        )
    }

    var host by remember {
        mutableStateOf(
            profile?.host ?: ""
        )
    }

    var port by remember {
        mutableStateOf(
            profile?.port?.toString() ?: "443"
        )
    }

    var uuid by remember {
        mutableStateOf(
            profile?.uuid ?: ""
        )
    }

    var security by remember {
        mutableStateOf(
            profile?.security ?: "tls"
        )
    }

    var network by remember {
        mutableStateOf(
            profile?.network ?: "tcp"
        )
    }

    var sni by remember {
        mutableStateOf(
            profile?.sni ?: ""
        )
    }

    var path by remember {
        mutableStateOf(
            profile?.path ?: ""
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp),
            verticalArrangement =
                Arrangement.Top
        ) {

            Text(
                text =
                    if (profile == null)
                        "Add Profile"
                    else
                        "Edit Profile",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Field(
                "Profile Name",
                name
            ) {
                name = it
            }

            Field(
                "Protocol",
                protocol
            ) {
                protocol = it
            }

            Field(
                "Host",
                host
            ) {
                host = it
            }

            Field(
                "Port",
                port
            ) {
                port = it
            }

            Field(
                "UUID / Password",
                uuid
            ) {
                uuid = it
            }

            Field(
                "Security",
                security
            ) {
                security = it
            }

            Field(
                "Network",
                network
            ) {
                network = it
            }

            Field(
                "SNI",
                sni
            ) {
                sni = it
            }

            Field(
                "Path",
                path
            ) {
                path = it
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick = {
		    android.util.Log.e(
 		        "MYXRAY",
   		        "SAVE BUTTON CLICKED"
		  )

                    if (profile == null) {

                        vm.addProfile(
                            name = name,
                            protocol = protocol,
                            host = host,
                            port = port.toIntOrNull()
                                ?: 443,
                            uuid = uuid,
                            security = security,
                            network = network,
                            sni = sni,
                            path = path
                        )

                    } else {

                        vm.updateProfile(
                            profile.copy(
                                name = name,
                                protocol = protocol,
                                host = host,
                                port =
                                    port.toIntOrNull()
                                        ?: 443,
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

                Text(
                    "Save"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick =
                    onCancel
            ) {

                Text(
                    "Cancel"
                )
            }
        }
    }
}

@Composable
private fun Field(
    title: String,
    value: String,
    onChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier =
            Modifier.fillMaxWidth(),
        value = value,
        onValueChange =
            onChange,
        label = {
            Text(title)
        }
    )

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )
}
