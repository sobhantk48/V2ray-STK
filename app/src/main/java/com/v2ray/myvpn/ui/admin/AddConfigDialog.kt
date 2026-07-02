package com.v2ray.myvpn.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.myvpn.data.Profile
import com.v2ray.myvpn.ui.theme.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun AddConfigDialog(
    onDismiss: () -> Unit,
    onAdd: (Profile) -> Unit,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("VLESS") }
    var jsonInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Config", color = WhiteText) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // دکمه‌های سریع
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* TODO: پیاده‌سازی QR Scanner */ },
                        enabled = hasCameraPermission,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Scan QR", color = WhiteText)
                    }
                    Button(
                        onClick = {
                            // پارس JSON
                            try {
                                val json = Json.parseToJsonElement(jsonInput).jsonObject
                                val nameFromJson = json["name"]?.jsonPrimitive?.content ?: "Imported"
                                val addressFromJson = json["address"]?.jsonPrimitive?.content ?: ""
                                val portFromJson = json["port"]?.jsonPrimitive?.content?.toIntOrNull() ?: 443
                                val typeFromJson = json["type"]?.jsonPrimitive?.content ?: "VLESS"

                                name = nameFromJson
                                address = addressFromJson
                                port = portFromJson.toString()
                                type = typeFromJson
                            } catch (_: Exception) {
                                // خطای پارس
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanAccent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Parse JSON", color = DarkBackground)
                    }
                }

                OutlinedTextField(
                    value = jsonInput,
                    onValueChange = { jsonInput = it },
                    label = { Text("Paste JSON here", color = WhiteText.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(alpha = 0.3f),
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText
                    ),
                    maxLines = 4
                )

                Divider(color = WhiteText.copy(alpha = 0.2f))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = WhiteText.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(alpha = 0.3f),
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText
                    )
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address", color = WhiteText.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(alpha = 0.3f),
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText
                    )
                )
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port", color = WhiteText.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = WhiteText.copy(alpha = 0.3f),
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText
                    )
                )
                // انتخاب نوع (Dropdown)
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Protocol", color = WhiteText.copy(alpha = 0.7f)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = WhiteText.copy(alpha = 0.3f),
                            focusedTextColor = WhiteText,
                            unfocusedTextColor = WhiteText
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("VLESS", "VMESS", "Trojan", "Shadowsocks").forEach { protocol ->
                            DropdownMenuItem(
                                text = { Text(protocol, color = WhiteText) },
                                onClick = {
                                    type = protocol
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && address.isNotBlank() && port.isNotBlank()) {
                        val newProfile = Profile(
                            name = name,
                            type = type,
                            address = address,
                            port = port.toIntOrNull() ?: 443
                        )
                        onAdd(newProfile)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenAccent
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add", color = DarkBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = WhiteText)
            }
        },
        containerColor = DarkSurface,
        titleContentColor = WhiteText,
        textContentColor = WhiteText
    )
}
