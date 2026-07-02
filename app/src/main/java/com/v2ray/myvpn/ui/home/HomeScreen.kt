package com.v2ray.myvpn.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onConnect: () -> Unit = {},
    onAdmin: () -> Unit = {}
) {

    var connected by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val blue = Color(0xFF00C2FF)
    val silver = Color(0xFFC0C0C0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "V2RAY STK",
                        color = blue
                    )
                },
                actions = {

                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Text("⋮", fontSize = 22.sp, color = Color.White)
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Admin Panel") },
                                onClick = {
                                    menuExpanded = false
                                    onAdmin()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1117)
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0D1117)),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = if (connected) "CONNECTED" else "DISCONNECTED",
                    color = if (connected) blue else Color.Gray,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // ===== CONNECT BUTTON (LOGO STYLE) =====
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .shadow(20.dp, CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = if (connected)
                                    listOf(blue, silver)
                                else
                                    listOf(Color.DarkGray, Color.Black)
                            ),
                            shape = CircleShape
                        )
                        .clickable {
                            connected = !connected
                            onConnect()
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (connected) "CONNECTED" else "CONNECT",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "v1.0",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
