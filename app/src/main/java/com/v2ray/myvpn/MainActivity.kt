package com.v2ray.myvpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppScreen()
        }
    }
}

@Composable
fun AppScreen(
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
            verticalArrangement =
                Arrangement.Center,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "V2ray STK",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(
                text =
                    when (state) {
                        VpnState.DISCONNECTED ->
                            "Disconnected"

                        VpnState.CONNECTING ->
                            "Connecting..."

                        VpnState.CONNECTED ->
                            "Connected"

                        VpnState.DISCONNECTING ->
                            "Disconnecting..."
                    }
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Button(
                onClick = {
                    vm.toggle()
                }
            ) {
                Text(
                    when (state) {
                        VpnState.DISCONNECTED ->
                            "Connect"

                        VpnState.CONNECTING ->
                            "Connecting"

                        VpnState.CONNECTED ->
                            "Disconnect"

                        VpnState.DISCONNECTING ->
                            "Disconnecting"
                    }
                )
            }
        }
    }
}
