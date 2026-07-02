package com.v2ray.myvpn.ui.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.v2ray.myvpn.viewmodel.ProfileViewModel

@Composable
fun ProfilesScreen(
    vm: ProfileViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit
) {
    val profiles by vm.profiles.collectAsState()

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
                .padding(16.dp)
        ) {

            Text(
                text = "Profiles (${profiles.size})",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            if (profiles.isEmpty()) {
                Text("No profiles yet")
            } else {
                profiles.forEach { profile ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(profile.name)

                            Row {
                                TextButton(
                                    onClick = { onEdit(profile.id) }
                                ) {
                                    Text("Edit")
                                }

                                TextButton(
                                    onClick = { vm.delete(profile.id) }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
