package com.v2ray.myvpn.repository

import android.content.Context
import android.util.Log
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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ProfileRepository {

    private const val TAG = "PROFILE_REPO"

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

        Log.e(TAG, "INITIALIZE CALLED")

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

                Log.e(
                    TAG,
                    "RAW DATA = $raw"
                )

                if (
                    !raw.isNullOrBlank()
                ) {

                    _profiles.value =
                        json.decodeFromString(
                            raw
                        )

                    Log.e(
                        TAG,
                        "LOADED = ${_profiles.value.size}"
                    )
                }

            } catch (
                e: Exception
            ) {

                Log.e(
                    TAG,
                    "LOAD ERROR",
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

                Log.e(
                    TAG,
                    "SAVE = $raw"
                )

                context
                    .profileDataStore
                    .edit { prefs ->

                        prefs[
                            ProfileKeys
                                .PROFILES
                        ] = raw
                    }

                Log.e(
                    TAG,
                    "SAVE SUCCESS"
                )

            } catch (
                e: Exception
            ) {

                Log.e(
                    TAG,
                    "SAVE ERROR",
                    e
                )
            }
        }
    }

    fun getAll(): List<Profile> =
        _profiles.value

    fun getSelected(): Profile? =
        _profiles.value
            .firstOrNull {
                it.selected
            }

    fun add(
        profile: Profile
    ) {

        _profiles.update {
            it + profile
        }

        Log.e(
            TAG,
            "ADD ${profile.name}"
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

    fun count(): Int =
        _profiles.value.size
}
