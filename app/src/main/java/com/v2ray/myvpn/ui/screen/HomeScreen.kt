package com.v2ray.myvpn.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    onConnect: () -> Unit,
    vm: MainViewModel = viewModel()
) {

    val state by
        vm.vpnState.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "V2ray STK",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(16.dp),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp),

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Server"
                )

                Text(
                    text = "---",
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text =
                            "Ping : ---"
                    )

                    Text(
                        text =
                            "Status : " +
                                when (state) {

                                    VpnState.DISCONNECTED ->
                                        "Offline"

                                    VpnState.CONNECTING ->
                                        "Connecting"

                                    VpnState.CONNECTED ->
                                        "Online"

                                    VpnState.DISCONNECTING ->
                                        "Disconnecting"
                                }
                    )
                }

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text =
                            "Upload : ---"
                    )

                    Text(
                        text =
                            "Download : ---"
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            onClick = {

                if (
                    state ==
                    VpnState.DISCONNECTED
                ) {
                    onConnect()
                }

                vm.toggle()
            }
        ) {

            Text(
                text =
                    when (state) {

                        VpnState.DISCONNECTED ->
                            "CONNECT"

                        VpnState.CONNECTING ->
                            "CONNECTING..."

                        VpnState.CONNECTED ->
                            "DISCONNECT"

                        VpnState.DISCONNECTING ->
                            "DISCONNECTING..."
                    }
            )
        }
    }
}
