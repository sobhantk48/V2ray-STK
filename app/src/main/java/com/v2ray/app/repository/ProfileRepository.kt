package com.v2ray.app.repository

import android.content.Context
import android.content.SharedPreferences
import com.v2ray.app.data.Profile
import com.v2ray.app.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

object ProfileRepository {
    private lateinit var prefs: SharedPreferences
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles

    fun initialize(ctx: Context) {
        prefs = ctx.getSharedPreferences("v2ray_profiles", Context.MODE_PRIVATE)
        Logger.writeLog("ProfileRepository initialized with SharedPreferences")
        loadFromStorage()
    }

    private fun loadFromStorage() {
        try {
            val raw = prefs.getString("profiles", null)
            if (!raw.isNullOrBlank()) {
                _profiles.value = json.decodeFromString(raw)
                Logger.writeLog("Loaded ${_profiles.value.size} profiles from SharedPreferences")
            } else {
                Logger.writeLog("No profiles found in SharedPreferences")
            }
        } catch (e: Exception) {
            Logger.writeError("Load profiles failed", e)
        }
    }

    private fun saveToStorage() {
        try {
            val raw = json.encodeToString(_profiles.value)
            prefs.edit().putString("profiles", raw).apply()
            Logger.writeLog("Saved ${_profiles.value.size} profiles to SharedPreferences")
        } catch (e: Exception) {
            Logger.writeError("Save profiles failed", e)
        }
    }

    private fun save() = CoroutineScope(Dispatchers.IO).launch { saveToStorage() }

    fun add(profile: Profile) {
        _profiles.update { it + profile }
        save()
        Logger.writeLog("Added: ${profile.name}")
    }

    fun update(profile: Profile) {
        _profiles.update { list ->
            list.map { if (it.id == profile.id) profile else it }
        }
        save()
    }

    fun delete(id: String) {
        _profiles.update { it.filterNot { it.id == id } }
        save()
        Logger.writeLog("Deleted: $id")
    }

    fun select(id: String) {
        _profiles.update { list ->
            list.map { it.copy(selected = it.id == id) }
        }
        save()
        Logger.writeLog("Selected profile: $id")
    }

    fun getSelected(): Profile? = _profiles.value.firstOrNull { it.selected }

    fun clear() {
        _profiles.value = emptyList()
        save()
    }
}
