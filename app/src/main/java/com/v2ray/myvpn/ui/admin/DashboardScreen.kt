package com.v2ray.myvpn.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    profileCount: Int = 0,
    subscriptionCount: Int = 0,
    activeProfile: String = "None",
    onProfiles: () -> Unit = {},
    onSubscription: () -> Unit = {},
    onLogs: () -> Unit = {},
    onSettings: () -> Unit = {},
    onSecurity: () -> Unit = {},
    onAbout: () -> Unit = {},
    onLogout: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "V2ray STK Admin",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                DashboardCard(
                    title = "Profiles",
                    value = profileCount.toString(),
                    modifier = Modifier.weight(1f)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                DashboardCard(
                    title = "Subscriptions",
                    value = subscriptionCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            DashboardCard(
                title = "Active Profile",
                value = activeProfile,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onProfiles
            ) {
                Text("Manage Profiles")
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubscription
            ) {
                Text("Manage Subscriptions")
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogs
            ) {
                Text("View Logs")
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSettings
            ) {
                Text("Settings")
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSecurity
            ) {
                Text("Security")
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAbout
            ) {
                Text("About")
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogout
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
