package com.v2ray.myvpn.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.v2ray.myvpn.repository.ProfileRepository

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}

@Composable
fun HomeScreen(
    onConnect: () -> Unit = {},
    onAdmin: () -> Unit = {}
) {

    var state by remember { mutableStateOf(ConnectionState.DISCONNECTED) }

    val selectedProfile = ProfileRepository.getSelected()

    val scale by animateFloatAsState(
        targetValue = if (state == ConnectionState.CONNECTING) 1.08f else 1f,
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F14))
    ) {

        // ☰ Admin menu
        TextButton(
            onClick = onAdmin,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Text(
                text = "⋮",
                color = Color(0xFFB0BEC5),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔵 Status
            Text(
                text = when (state) {
                    ConnectionState.DISCONNECTED -> "DISCONNECTED"
                    ConnectionState.CONNECTING -> "CONNECTING..."
                    ConnectionState.CONNECTED -> "CONNECTED"
                },
                color = when (state) {
                    ConnectionState.CONNECTED -> Color(0xFF4FC3F7)
                    ConnectionState.CONNECTING -> Color(0xFF90CAF9)
                    ConnectionState.DISCONNECTED -> Color(0xFFB0BEC5)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 📡 Profile info (REAL DATA)
            if (selectedProfile != null) {

                Text(
                    text = selectedProfile.name,
                    color = Color.White
                )

                Text(
                    text = "${selectedProfile.host}:${selectedProfile.port}",
                    color = Color(0xFFB0BEC5)
                )

            } else {

                Text(
                    text = "No Profile Selected",
                    color = Color(0xFFB0BEC5)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // 🔵 CONNECT BUTTON
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .shadow(
                        30.dp,
                        CircleShape,
                        spotColor = Color(0xFF4FC3F7)
                    )
                    .background(
                        Color(0xFF121A22),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Button(
                    onClick = {

                        // 🚀 فقط اگر پروفایل داریم وصل شو
                        if (selectedProfile != null) {

                            state =
                                when (state) {
                                    ConnectionState.DISCONNECTED -> ConnectionState.CONNECTING
                                    ConnectionState.CONNECTING -> ConnectionState.CONNECTED
                                    ConnectionState.CONNECTED -> ConnectionState.DISCONNECTED
                                }

                            onConnect()
                        }
                    },
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (state) {
                            ConnectionState.DISCONNECTED -> Color(0xFF1E293B)
                            ConnectionState.CONNECTING -> Color(0xFF3B82F6)
                            ConnectionState.CONNECTED -> Color(0xFF4FC3F7)
                        }
                    )
                ) {

                    Text("CONNECT")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Admin shortcut
            TextButton(onClick = onAdmin) {
                Text(
                    text = "Admin",
                    color = Color(0xFFB0BEC5)
                )
            }
        }
    }
}
