package com.v2ray.myvpn

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.viewmodel.MainViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppScreen(
                onConnect = {
                    val intent =
                        VpnService.prepare(this)

                    if (intent != null) {
                        vpnPermission.launch(intent)
                    } else {
                        startService(
                            Intent(
                                this,
                                MyVpnService::class.java
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AppScreen(
    onConnect: () -> Unit,
    vm: MainViewModel = viewModel()
) {
    val state by vm.vpnState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "V2ray STK",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = when (state) {
                    VpnState.DISCONNECTED -> "Disconnected"
                    VpnState.CONNECTING -> "Connecting..."
                    VpnState.CONNECTED -> "Connected"
                    VpnState.DISCONNECTING -> "Disconnecting..."
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = {
                    onConnect()
                    vm.toggle()
                }
            ) {
                Text(
                    when (state) {
                        VpnState.DISCONNECTED -> "Connect"
                        VpnState.CONNECTING -> "Connecting"
                        VpnState.CONNECTED -> "Disconnect"
                        VpnState.DISCONNECTING -> "Disconnecting"
                    }
                )
            }
        }
    }
}
