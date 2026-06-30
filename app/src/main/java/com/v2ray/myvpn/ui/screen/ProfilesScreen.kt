package com.v2ray.myvpn.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.v2ray.myvpn.repository.ServerRepository

@Composable
fun ProfilesScreen() {

    val servers by ServerRepository.servers.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp)
    ) {

        Text(
            text = "Profiles"
        )

        if (servers.isEmpty()) {

            Text(
                text = "No profiles"
            )

        } else {

            LazyColumn {

                items(servers) { server ->

                    Card(
                        modifier =
                            Modifier
                                .padding(vertical = 8.dp)
                    ) {

                        Column(
                            modifier =
                                Modifier
                                    .padding(16.dp)
                        ) {

                            Text(
                                text = server.remarks
                            )

                            Text(
                                text = "${server.protocol}://${server.address}:${server.port}"
                            )
                        }
                    }
                }
            }
        }
    }
}
