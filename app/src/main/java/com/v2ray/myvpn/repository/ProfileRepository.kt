package com.v2ray.myvpn.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.v2ray.myvpn.model.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ProfileRepository {

    private lateinit var context: Context

    private val scope =
        CoroutineScope(
            SupervisorJob() +
            Dispatchers.IO
        )

    private val json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }

    private val _profiles =
        MutableStateFlow<List<Profile>>(
            emptyList()
        )

    val profiles: StateFlow<List<Profile>>
        get() = _profiles

    fun initialize(
        ctx: Context
    ) {

        context =
            ctx.applicationContext

        scope.launch {

            try {

                val prefs =
                    context
                        .profileDataStore
                        .data
                        .first()

                val raw =
                    prefs[
                        ProfileKeys
                            .PROFILES
                    ]

                if (
                    !raw.isNullOrBlank()
                ) {

                    _profiles.value =
                        json.decodeFromString(
                            raw
                        )
                }

            } catch (
                _: Exception
            ) {

                _profiles.value =
                    emptyList()
            }
        }
    }

    private fun save() {

        scope.launch {

            try {

                context
                    .profileDataStore
                    .edit { prefs ->

                        prefs[
                            ProfileKeys
                                .PROFILES
                        ] =
                            json.encodeToString(
                                _profiles.value
                            )
                    }

            } catch (
                _: Exception
            ) {
            }
        }
    }

    fun getAll():
        List<Profile> {

        return _profiles.value
    }

    fun getSelected():
        Profile? {

        return _profiles
            .value
            .firstOrNull {
                it.selected
            }
    }

    fun add(
        profile: Profile
    ) {

        _profiles.update {
            it + profile
        }

        save()
    }

    fun update(
        profile: Profile
    ) {

        _profiles.update { list ->

            list.map {

                if (
                    it.id ==
                    profile.id
                ) {
                    profile
                } else {
                    it
                }
            }
        }

        save()
    }

    fun delete(
        profileId: String
    ) {

        _profiles.update {

            it.filterNot {
                profile ->

                profile.id ==
                    profileId
            }
        }

        save()
    }

    fun select(
        profileId: String
    ) {

        _profiles.update { list ->

            list.map {

                it.copy(
                    selected =
                        it.id ==
                        profileId
                )
            }
        }

        save()
    }

    fun clear() {

        _profiles.value =
            emptyList()

        save()
    }

    fun count():
        Int {

        return _profiles
            .value
            .size
    }
}
