package com.v2ray.myvpn.repository

import android.content.Context
import com.v2ray.myvpn.model.Profile
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
import androidx.datastore.preferences.core.edit
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

    private fun log(
        text: String
    ) {

        try {

            val file =
                File(
                    context.filesDir,
                    "profile_debug.log"
                )

            file.appendText(
                "${System.currentTimeMillis()} : $text\n"
            )

        } catch (_: Exception) {
        }
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

        log(
            "initialize()"
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
                    "loaded raw=$raw"
                )

                if (
                    !raw.isNullOrBlank()
                ) {

                    _profiles.value =
                        json.decodeFromString(
                            raw
                        )

                    log(
                        "loaded count=${_profiles.value.size}"
                    )
                }

            } catch (
                e: Exception
            ) {

                log(
                    "initialize error=${e}"
                )
            }
        }
    }

    private fun save() {

        val dump =
            json.encodeToString(
                _profiles.value
            )

        log(
            "save() count=${_profiles.value.size}"
        )

        scope.launch {

            try {

                context
                    .profileDataStore
                    .edit { prefs ->

                        prefs[
                            ProfileKeys.PROFILES
                        ] = dump
                    }

                log(
                    "save success"
                )

            } catch (
                e: Exception
            ) {

                log(
                    "save error=${e}"
                )
            }
        }
    }

    fun add(
        profile: Profile
    ) {

        log(
            "add ${profile.name}"
        )

        _profiles.update {
            it + profile
        }

        log(
            "after add=${_profiles.value.size}"
        )

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
                )
                    profile
                else
                    it
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

    fun getSelected():
        Profile? {

        return _profiles
            .value
            .firstOrNull {
                it.selected
            }
    }

    fun count():
        Int {

        return _profiles
            .value
            .size
    }

    fun clear() {

        _profiles.value =
            emptyList()

        save()
    }
}
