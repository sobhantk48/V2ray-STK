package com.v2ray.myvpn.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "V2ray STK",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            AboutItem(
                "Version",
                "1.0.0"
            )

            AboutItem(
                "Build",
                "Alpha"
            )

            AboutItem(
                "Developer",
                "Sobhan TK"
            )

            AboutItem(
                "Core",
                "Xray-core"
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Supported Protocols",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            AboutItem(
                "VLESS",
                "Supported"
            )

            AboutItem(
                "VMESS",
                "Supported"
            )

            AboutItem(
                "Trojan",
                "Planned"
            )

            AboutItem(
                "Shadowsocks",
                "Planned"
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            AboutItem(
                "License",
                "GPL-3.0"
            )

            AboutItem(
                "Github",
                "V2ray-STK"
            )
        }
    }
}

@Composable
private fun AboutItem(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value
            )
        }
    }
}
