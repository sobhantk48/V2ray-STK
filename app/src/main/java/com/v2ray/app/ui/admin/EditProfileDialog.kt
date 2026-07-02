package com.v2ray.app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v2ray.app.data.Profile
import com.v2ray.app.ui.theme.*

@Composable
fun EditProfileDialog(profile: Profile, onDismiss: () -> Unit, onSave: (Profile) -> Unit) {
    var name by remember { mutableStateOf(profile.name) }
    var addr by remember { mutableStateOf(profile.address) }
    var port by remember { mutableStateOf(profile.port.toString()) }
    var type by remember { mutableStateOf(profile.type) }
    var uuid by remember { mutableStateOf(profile.uuid) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Edit Profile", color = WhiteText) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Name", color = WhiteText.copy(0.7f)) }, modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(0.3f), focusedTextColor = WhiteText, unfocusedTextColor = WhiteText))
                OutlinedTextField(addr, { addr = it }, label = { Text("Address", color = WhiteText.copy(0.7f)) }, modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(0.3f), focusedTextColor = WhiteText, unfocusedTextColor = WhiteText))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(port, { port = it }, label = { Text("Port", color = WhiteText.copy(0.7f)) }, modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = WhiteText.copy(0.3f), focusedTextColor = WhiteText, unfocusedTextColor = WhiteText))
                    OutlinedTextField(uuid, { uuid = it }, label = { Text("UUID", color = WhiteText.copy(0.7f)) }, modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = WhiteText.copy(0.3f), focusedTextColor = WhiteText, unfocusedTextColor = WhiteText))
                }
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded, { expanded = it }) {
                    OutlinedTextField(type, {}, readOnly = true, label = { Text("Protocol", color = WhiteText.copy(0.7f)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = WhiteText.copy(0.3f), focusedTextColor = WhiteText, unfocusedTextColor = WhiteText))
                    ExposedDropdownMenu(expanded, { expanded = false }) {
                        listOf("VLESS", "VMESS", "Trojan", "Shadowsocks").forEach {
                            DropdownMenuItem({ Text(it, color = WhiteText) }, onClick = { type = it; expanded = false })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button({
                onSave(profile.copy(name = name, address = addr, port = port.toIntOrNull() ?: 443, type = type, uuid = uuid))
            }, colors = ButtonDefaults.buttonColors(PrimaryBlue), shape = RoundedCornerShape(8.dp)) {
                Text("Save", color = WhiteText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onDismiss) { Text("Cancel", color = WhiteText) } },
        containerColor = DarkSurface, titleContentColor = WhiteText, textContentColor = WhiteText
    )
}
