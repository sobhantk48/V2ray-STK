package com.v2ray.myvpn.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfilesScreen() {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp)
    ) {

        Text(
            text = "Profiles"
        )

        Text(
            text = "No profiles"
        )
    }
}
