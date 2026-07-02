package com.v2ray.myvpn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Blue / Silver theme (based on your logo)
private val DarkColors = darkColorScheme(
    primary = Color(0xFF4FC3F7),        // Blue Light
    onPrimary = Color(0xFF001018),

    secondary = Color(0xFFB0BEC5),      // Silver
    onSecondary = Color(0xFF0A0A0A),

    background = Color(0xFF0B0F14),     // Dark background
    onBackground = Color(0xFFE6F1FF),

    surface = Color(0xFF121A22),
    onSurface = Color(0xFFE6F1FF),

    tertiary = Color(0xFF2196F3)        // Strong Blue accent
)

@Composable
fun V2raySTKTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
