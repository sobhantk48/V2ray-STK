package com.v2ray.myvpn.ui.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfilesScreen(
    profiles: List<Any> = emptyList(),
    onAdd: () -> Unit = {}
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 70.dp) // 👈 مهم
        ) {

            Text(
                text = "Profiles (${profiles.size})",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            if (profiles.isEmpty()) {
                Text("No profiles yet", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
