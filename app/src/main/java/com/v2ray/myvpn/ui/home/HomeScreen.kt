package com.v2ray.myvpn.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onConnect: () -> Unit = {},
    onAdmin: () -> Unit = {}
) {

    var connected by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0D1117)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = if (connected) "CONNECTED" else "DISCONNECTED",
                fontSize = 20.sp,
                color = if (connected) Color(0xFF00C2FF) else Color.Gray
            )

            Button(
                onClick = {
                    connected = !connected
                    onConnect()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(160.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (connected)
                        Color(0xFF00C2FF)
                    else
                        Color(0xFF2D333B)
                )
            ) {
                Text(
                    text = if (connected) "CONNECTED" else "CONNECT",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(onClick = onAdmin) {
                    Text("ADMIN", color = Color(0xFF00C2FF))
                }

                Text(
                    text = "v1.0",
                    color = Color.Gray
                )
            }
        }
    }
}
