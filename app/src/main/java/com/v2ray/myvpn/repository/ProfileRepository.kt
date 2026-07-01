package com.v2ray.myvpn.repository

import android.content.Context
import com.v2ray.myvpn.model.Profile
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

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

    private fun log(
        text: String
    ) {

        try {

            val file =
                File(
                    "/storage/emulated/0/MyXrayVPN_repository.log"
                )

            file.appendText(
                "${System.currentTimeMillis()} : $text\n"
            )

        } catch (_: Exception) {
        }
    }

    private fun logError(
        where: String,
        e: Exception
    ) {

        try {

            val file =
                File(
                    "/storage/emulated/0/MyXrayVPN_repository.log"
                )

            file.appendText(
                "\n==========\n"
            )

            file.appendText(
                "$where ERROR\n"
            )

            file.appendText(
                e.stackTraceToString()
            )

            file.appendText(
                "\n==========\n"
            )

        } catch (_: Exception) {
        }
    }

    fun initialize(
        ctx: Context
    ) {

        context =
            ctx.applicationContext

        log(
            "INITIALIZE CALLED"
        )

        scope.launch {

            try {

                val prefs =
                    context
                        .profileDataStore
                        .data
                        .first()

                val raw =
                    prefs[
                        ProfileKeys.PROFILES
                    ]

                log(
                    "RAW = $raw"
                )

                if (
                    !raw.isNullOrBlank()
                ) {

                    _profiles.value =
                        json.decodeFromString(
                            raw
                        )

                    log(
                        "LOADED ${_profiles.value.size}"
                    )
                }

            } catch (
                e: Exception
            ) {

                logError(
                    "LOAD",
                    e
                )

                _profiles.value =
                    emptyList()
            }
        }
    }

    private fun save() {

        scope.launch {

            try {

                val raw =
                    json.encodeToString(
                        _profiles.value
                    )

                log(
                    "SAVE START"
                )

                log(
                    raw
                )

                context
                    .profileDataStore
                    .edit { prefs ->

                        prefs[
                            ProfileKeys.PROFILES
                        ] = raw
                    }

                log(
                    "SAVE SUCCESS"
                )

            } catch (
                e: Exception
            ) {

                logError(
                    "SAVE",
                    e
                )
            }
        }
    }

    fun getAll():
        List<Profile> {

        return _profiles.value
    }

    fun getSelected():
        Profile? {

        return _profiles.value
            .firstOrNull {
                it.selected
            }
    }

    fun add(
        profile: Profile
    ) {

        log(
            "ADD ${profile.name}"
        )

        _profiles.update {
            it + profile
        }

        save()
    }

    fun update(
        profile: Profile
    ) {

        log(
            "UPDATE ${profile.name}"
        )

        _profiles.update { list ->

            list.map {

                if (
                    it.id ==
                    profile.id
                ) profile
                else it
            }
        }

        save()
    }

    fun delete(
        profileId: String
    ) {

        log(
            "DELETE $profileId"
        )

        _profiles.update {

            it.filterNot {
                p ->
                p.id ==
                    profileId
            }
        }

        save()
    }

    fun select(
        profileId: String
    ) {

        log(
            "SELECT $profileId"
        )

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

        log(
            "CLEAR"
        )

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
