package com.v2ray.myvpn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.myvpn.data.Profile
import com.v2ray.myvpn.repository.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ConnectionStatus {
    IDLE, CONNECTING, CONNECTED, DISCONNECTED
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val profiles: StateFlow<List<Profile>> = ProfileRepository.profiles

    // تنظیمات
    val autoConnect = MutableStateFlow(false)
    val notificationsEnabled = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            profiles.collect { list ->
                val selected = list.firstOrNull { it.selected }
                _uiState.update { it.copy(currentProfile = selected) }
            }
        }
    }

    data class UiState(
        val status: ConnectionStatus = ConnectionStatus.IDLE,
        val currentProfile: Profile? = null,
        val connectedTime: String = "00:00:00",
        val ping: Int = 0,
        val downloadSpeed: Double = 0.0,
        val uploadSpeed: Double = 0.0
    )

    fun connect() {
        if (_uiState.value.currentProfile == null) return
        _uiState.update { it.copy(status = ConnectionStatus.CONNECTING) }
        // شبیه‌سازی اتصال
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _uiState.update {
                it.copy(
                    status = ConnectionStatus.CONNECTED,
                    connectedTime = "00:12:45",
                    ping = 42,
                    downloadSpeed = 25.6,
                    uploadSpeed = 8.4
                )
            }
        }
    }

    fun disconnect() {
        _uiState.update {
            it.copy(
                status = ConnectionStatus.DISCONNECTED,
                connectedTime = "00:00:00",
                ping = 0,
                downloadSpeed = 0.0,
                uploadSpeed = 0.0
            )
        }
    }

    fun selectProfile(profileId: String) {
        ProfileRepository.select(profileId)
        val selected = ProfileRepository.getSelected()
        _uiState.update { it.copy(currentProfile = selected) }
    }

    fun addProfile(profile: Profile) {
        ProfileRepository.add(profile)
    }

    fun updateProfile(profile: Profile) {
        ProfileRepository.update(profile)
    }

    fun deleteProfile(profileId: String) {
        ProfileRepository.delete(profileId)
    }

    fun setAutoConnect(enabled: Boolean) {
        autoConnect.value = enabled
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        notificationsEnabled.value = enabled
    }
}
