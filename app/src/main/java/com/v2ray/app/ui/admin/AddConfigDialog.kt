package com.v2ray.app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v2ray.app.data.Profile
import com.v2ray.app.ui.theme.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddConfigDialog(onDismiss: () -> Unit, onAdd: (Profile) -> Unit) {
    var name by remember { mutableStateOf("") }
    var addr by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("443") }
    var type by remember { mutableStateOf("VLESS") }
    var uuid by remember { mutableStateOf("") }
    var jsonInput by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Add Config", color = WhiteText) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(jsonInput, { jsonInput = it }, label = { Text("Paste JSON or Link", color = WhiteText.copy(0.7f)) },
                    modifier = Modifier.fillMaxWidth(), maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(0.3f), focusedTextColor = WhiteText, unfocusedTextColor = WhiteText))
                Button({
                    try {
                        Profile.fromLink(jsonInput)?.let { p ->
                            name = p.name; addr = p.address; port = p.port.toString(); type = p.type; uuid = p.uuid
                        } ?: run {
                            val obj = Json.parseToJsonElement(jsonInput).jsonObject
                            name = obj["name"]?.jsonPrimitive?.content ?: name
                            addr = obj["address"]?.jsonPrimitive?.content ?: addr
                            port = obj["port"]?.jsonPrimitive?.content?.toString() ?: port
                            type = obj["type"]?.jsonPrimitive?.content?.uppercase() ?: type
                            uuid = obj["uuid"]?.jsonPrimitive?.content ?: ""
                        }
                    } catch (_: Exception) {}
                }, colors = ButtonDefaults.buttonColors(CyanAccent), shape = RoundedCornerShape(8.dp), modifier = Modifier.align(Alignment.End)) {
                    Text("Parse", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
                Divider(color = WhiteText.copy(0.2f))
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
                if (name.isNotBlank() && addr.isNotBlank() && port.isNotBlank()) {
                    onAdd(Profile(name = name, type = type, address = addr, port = port.toIntOrNull() ?: 443, uuid = uuid))
                }
            }, colors = ButtonDefaults.buttonColors(GreenAccent), shape = RoundedCornerShape(8.dp)) {
                Text("Add", color = DarkBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onDismiss) { Text("Cancel", color = WhiteText) } },
        containerColor = DarkSurface, titleContentColor = WhiteText, textContentColor = WhiteText
    )
}
