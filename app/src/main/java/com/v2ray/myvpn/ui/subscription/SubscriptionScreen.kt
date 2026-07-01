package com.v2ray.myvpn.ui.subscription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.Subscription
import com.v2ray.myvpn.viewmodel.SubscriptionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SubscriptionScreen(
    vm: SubscriptionViewModel = viewModel()
) {

    val subscriptions by
        vm.subscriptions.collectAsState()

    val name =
        remember {
            mutableStateOf("")
        }

    val url =
        remember {
            mutableStateOf("")
        }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Subscriptions",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            OutlinedTextField(
                modifier =
                    Modifier.fillMaxWidth(),
                value = name.value,
                onValueChange = {
                    name.value = it
                },
                label = {
                    Text("Name")
                }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            OutlinedTextField(
                modifier =
                    Modifier.fillMaxWidth(),
                value = url.value,
                onValueChange = {
                    url.value = it
                },
                label = {
                    Text(
                        "Subscription URL"
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick = {

                    vm.addSubscription(
                        name.value,
                        url.value
                    )

                    name.value = ""
                    url.value = ""
                }
            ) {
                Text(
                    "Add Subscription"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            LazyColumn(
                modifier =
                    Modifier.weight(1f)
            ) {

                items(
                    subscriptions
                ) { sub ->

                    SubscriptionCard(
                        subscription = sub,
                        onEnable = {

                            vm.enableSubscription(
                                sub.id,
                                !sub.enabled
                            )
                        },
                        onRefresh = {

                            vm.refreshSubscription(
                                sub.id,
                                sub.profilesCount
                            )
                        },
                        onDelete = {

                            vm.removeSubscription(
                                sub.id
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscriptionCard(
    subscription: Subscription,
    onEnable: () -> Unit,
    onRefresh: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 12.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    subscription.name,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    subscription.url
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Profiles : ${
                        subscription
                            .profilesCount
                    }"
            )

            Text(
                text =
                    "Last Update : ${
                        if (
                            subscription
                                .lastUpdate == 0L
                        ) {
                            "Never"
                        } else {
                            SimpleDateFormat(
                                "yyyy/MM/dd HH:mm",
                                Locale.getDefault()
                            ).format(
                                Date(
                                    subscription
                                        .lastUpdate
                                )
                            )
                        }
                    }"
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Enabled",
                    modifier =
                        Modifier.weight(1f)
                )

                Switch(
                    checked =
                        subscription.enabled,
                    onCheckedChange = {
                        onEnable()
                    }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick = onRefresh
            ) {
                Text("Refresh")
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}
