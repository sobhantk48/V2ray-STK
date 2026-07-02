package com.v2ray.myvpn.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profiles")

fun Context.getProfileDataStore(): DataStore<Preferences> = profileDataStore
