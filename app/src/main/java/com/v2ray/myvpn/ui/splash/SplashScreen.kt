package com.v2ray.myvpn.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: () -> Unit = {}
) {

    LaunchedEffect(Unit) {

        delay(1500)

        onFinish()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "MyXrayVPN",
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        CircularProgressIndicator()
    }
}
