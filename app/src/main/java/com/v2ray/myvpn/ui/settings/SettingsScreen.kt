package com.v2ray.myvpn.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.myvpn.ui.theme.*
import com.v2ray.myvpn.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedProtocol by remember { mutableStateOf("VLESS") }
    val protocols = listOf("VLESS", "VMESS", "Trojan", "Shadowsocks", "Socks")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = WhiteText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back", tint = WhiteText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Protocol", color = WhiteText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        protocols.forEach { protocol ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedProtocol = protocol }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(protocol, color = WhiteText)
                                if (selectedProtocol == protocol) {
                                    Icon(
                                        androidx.compose.material.icons.Icons.Default.Check,
                                        contentDescription = null,
                                        tint = CyanAccent
                                    )
                                }
                            }
                            Divider(color = WhiteText.copy(alpha = 0.1f))
                        }
                    }
                }
            }

            item {
                SwitchSetting(
                    title = "Auto Connect on Start",
                    checked = viewModel.autoConnect.value,
                    onCheckedChange = { viewModel.setAutoConnect(it) }
                )
            }

            item {
                SwitchSetting(
                    title = "Notifications",
                    checked = viewModel.notificationsEnabled.value,
                    onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                )
            }

            item {
                SwitchSetting(
                    title = "Dark Theme",
                    checked = true, // همیشه دارک
                    onCheckedChange = {}
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("DNS", color = WhiteText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("Auto", "1.1.1.1", "8.8.8.8", "Custom").forEach { dns ->
                                TextButton(
                                    onClick = { /* انتخاب DNS */ },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = CyanAccent
                                    )
                                ) {
                                    Text(dns)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwitchSetting(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, color = WhiteText)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CyanAccent,
                    uncheckedThumbColor = Color.Gray
                )
            )
        }
    }
}
