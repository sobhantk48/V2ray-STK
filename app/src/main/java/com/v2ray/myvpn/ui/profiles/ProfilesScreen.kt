package com.v2ray.myvpn.ui.profiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v2ray.myvpn.model.Profile
import com.v2ray.myvpn.viewmodel.ProfileViewModel

@Composable
fun ProfilesScreen(
    onAdd: () -> Unit = {},
    onEdit: (Profile) -> Unit = {},
    vm: ProfileViewModel = viewModel()
) {

    val profiles by
        vm.profiles.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text =
                    "Profiles (${profiles.size})",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            LazyColumn(
                modifier =
                    Modifier.weight(1f)
            ) {

                items(
                    profiles
                ) { profile ->

                    ProfileCard(
                        profile = profile,
                        onSelect = {
                            vm.selectProfile(
                                profile.id
                            )
                        },
                        onDelete = {
                            vm.deleteProfile(
                                profile.id
                            )
                        },
                        onEdit = {
                            onEdit(profile)
                        }
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick =
                    onAdd
            ) {

                Text(
                    "Add Profile"
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(
    profile: Profile,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
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
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text = profile.name,
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "${profile.host}:${profile.port}"
            )

            Text(
                text =
                    profile.country
            )

            if (
                profile.selected
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )

                Text(
                    text =
                        "ACTIVE",
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            Row {

                Button(
                    modifier =
                        Modifier.weight(
                            1f
                        ),
                    onClick =
                        onSelect
                ) {

                    Text(
                        "Select"
                    )
                }

                Spacer(
                    modifier =
                        Modifier.weight(
                            0.1f
                        )
                )

                Button(
                    modifier =
                        Modifier.weight(
                            1f
                        ),
                    onClick =
                        onEdit
                ) {

                    Text(
                        "Edit"
                    )
                }

                Spacer(
                    modifier =
                        Modifier.weight(
                            0.1f
                        )
                )

                Button(
                    modifier =
                        Modifier.weight(
                            1f
                        ),
                    onClick =
                        onDelete
                ) {

                    Text(
                        "Delete"
                    )
                }
            }
        }
    }
}
