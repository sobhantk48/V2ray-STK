package com.v2ray.myvpn.ui.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LogsScreen(
    logs: List<String> = listOf(
        "[12:00:01] Xray started",
        "[12:00:02] Connected",
        "[12:00:05] Ping 43ms",
        "[12:00:10] Download 5Mbps"
    ),
    onRefresh: () -> Unit = {},
    onClear: () -> Unit = {},
    onExport: () -> Unit = {}
) {

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
                text = "Logs",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(logs) { log ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {

                        Text(
                            text = log,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onRefresh
                ) {
                    Text("Refresh")
                }

                Spacer(
                    modifier = Modifier.weight(0.1f)
                )

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onClear
                ) {
                    Text("Clear")
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onExport
            ) {
                Text("Export")
            }
        }
    }
}
