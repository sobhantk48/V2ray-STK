package com.v2ray.myvpn.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onConnect: () -> Unit = {},
    onAdmin: () -> Unit = {}
) {

    var connected by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val blue = Color(0xFF00C2FF)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("V2RAY STK", color = blue)
                },
                actions = {
                    Box {
                        Text(
                            "⋮",
                            fontSize = 22.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(12.dp)
                                .clickable { menuExpanded = true }
                        )

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Admin") },
                                onClick = {
                                    menuExpanded = false
                                    onAdmin()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 70.dp), // 👈 مهم: جلوگیری از رفتن زیر nav bar
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (connected) "CONNECTED" else "DISCONNECTED",
                color = if (connected) blue else Color.Gray,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        color = if (connected) blue else Color.DarkGray,
                        shape = CircleShape
                    )
                    .clickable {
                        connected = !connected
                        onConnect()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CONNECT",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            Text("v1.0", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
