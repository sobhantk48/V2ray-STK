package com.v2ray.app.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.v2ray.app.data.Profile
import com.v2ray.app.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "v2ray_profiles")

object ProfileRepository {
    private lateinit var context: Context
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles

    fun initialize(ctx: Context) {
        context = ctx.applicationContext
        Logger.writeLog("ProfileRepository initialized")
        CoroutineScope(Dispatchers.IO).launch { loadFromStorage() }
    }

    private suspend fun loadFromStorage() {
        try {
            val raw = context.dataStore.data.first()[stringPreferencesKey("profiles")]
            if (!raw.isNullOrBlank()) {
                _profiles.value = json.decodeFromString(raw)
                Logger.writeLog("Loaded ${_profiles.value.size} profiles")
            }
        } catch (e: Exception) {
            Logger.writeError("Load profiles failed", e)
        }
    }

    private suspend fun saveToStorage() {
        try {
            val raw = json.encodeToString(_profiles.value)
            context.dataStore.edit { it[stringPreferencesKey("profiles")] = raw }
        } catch (e: Exception) {
            Logger.writeError("Save profiles failed", e)
        }
    }

    private fun save() = CoroutineScope(Dispatchers.IO).launch { saveToStorage() }

    fun add(profile: Profile) { _profiles.update { it + profile }; save(); Logger.writeLog("Added: ${profile.name}") }
    fun update(profile: Profile) { _profiles.update { list -> list.map { if (it.id == profile.id) profile else it } }; save() }
    fun delete(id: String) { _profiles.update { it.filterNot { it.id == id } }; save(); Logger.writeLog("Deleted: $id") }
    fun select(id: String) { _profiles.update { list -> list.map { it.copy(selected = it.id == id) } }; save() }
    fun getSelected(): Profile? = _profiles.value.firstOrNull { it.selected }
    fun clear() { _profiles.value = emptyList(); save() }
}
