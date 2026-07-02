package com.v2ray.myvpn.ui.profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.Profile
import com.v2ray.myvpn.viewmodel.ProfileViewModel

@Composable
fun ProfilesScreen(
    onAdd: () -> Unit = {},
    onEdit: (Profile) -> Unit = {},
    vm: ProfileViewModel = viewModel()
) {

    val profiles by vm.profiles.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Profiles (${profiles.size})",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 100.dp) // جلوگیری از زیر nav bar
            ) {

                items(profiles) { profile ->

                    ProfileCard(
                        profile = profile,
                        onSelect = { vm.selectProfile(profile.id) },
                        onDelete = { vm.deleteProfile(profile.id) },
                        onEdit = { onEdit(profile) }
                    )
                }
            }

            // 🔵 دکمه Add (دیگه زیر Nav نمی‌ره)
            Button(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text("Add Profile")
            }
        }
    }
}

@Composable
private fun ProfileCard(
    profile: Profile,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {

    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${profile.host}:${profile.port}",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = profile.country,
                        color = Color.Gray
                    )
                }

                Box {

                    TextButton(onClick = { menuExpanded = true }) {
                        Text("⋮", color = Color.White)
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Select") },
                            onClick = {
                                onSelect()
                                menuExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onEdit()
                                menuExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onDelete()
                                menuExpanded = false
                            }
                        )
                    }
                }
            }

            if (profile.selected) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ACTIVE",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
