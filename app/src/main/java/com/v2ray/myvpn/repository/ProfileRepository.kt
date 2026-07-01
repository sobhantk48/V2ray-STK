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

    private fun writeLog(
        msg: String
    ) {
        try {
            File(
                "/storage/emulated/0/MyXrayVPN_repository.log"
            ).appendText(
                "${System.currentTimeMillis()} : $msg\n"
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

        writeLog(
            "initialize called"
        )

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

                writeLog(
                    "raw=$raw"
                )

                if (
                    !raw.isNullOrBlank()
                ) {

                    _profiles.value =
                        json.decodeFromString(
                            raw
                        )

                    writeLog(
                        "loaded count=${_profiles.value.size}"
                    )
                }

            } catch (
                e: Exception
            ) {

                writeLog(
                    "initialize error=${e.message}"
                )

                _profiles.value =
                    emptyList()
            }
        }
    }

    private fun save() {

        writeLog(
            "save called count=${_profiles.value.size}"
        )

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

                writeLog(
                    "save success"
                )

            } catch (
                e: Exception
            ) {

                writeLog(
                    "save error=${e.message}"
                )
            }
        }
    }

    fun getAll(): List<Profile> {
        return _profiles.value
    }

    fun getSelected(): Profile? {
        return _profiles.value
            .firstOrNull {
                it.selected
            }
    }

    fun add(
        profile: Profile
    ) {

        writeLog(
            "add ${profile.name}"
        )

        _profiles.update {
            it + profile
        }

        writeLog(
            "after add count=${_profiles.value.size}"
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
                ) profile
                else it
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

    fun count(): Int {
        return _profiles
            .value
            .size
    }
}
