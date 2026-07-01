package com.v2ray.myvpn.repository

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.profileDataStore by preferencesDataStore(
    name = "profiles"
)

object ProfileKeys {
    val PROFILES =
        stringPreferencesKey(
            "profiles_json"
        )
}
