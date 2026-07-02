package com.v2ray.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.app.data.ConnectionState
import com.v2ray.app.data.ConnectionStatus
import com.v2ray.app.data.Profile
import com.v2ray.app.repository.ProfileRepository
import com.v2ray.app.service.V2RayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(ConnectionState())
    val state: StateFlow<ConnectionState> = _state.asStateFlow()
    private val _current = MutableStateFlow<Profile?>(null)
    val current: StateFlow<Profile?> = _current.asStateFlow()
    val profiles: StateFlow<List<Profile>> = ProfileRepository.profiles

    init {
        ProfileRepository.initialize(application)
        viewModelScope.launch {
            ProfileRepository.profiles.collect { list ->
                _current.value = list.firstOrNull { it.selected }
            }
        }
        V2RayService.observeState { newState ->
            _state.value = newState
        }
    }

    fun connect(profile: Profile) { V2RayService.start(getApplication(), profile) }
    fun disconnect() { V2RayService.stop(getApplication()) }
    fun select(id: String) { ProfileRepository.select(id); _current.value = ProfileRepository.getSelected() }
    fun add(profile: Profile) { ProfileRepository.add(profile) }
    fun update(profile: Profile) { ProfileRepository.update(profile) }
    fun delete(id: String) { ProfileRepository.delete(id) }
    fun clearError() { _state.update { it.copy(errorMessage = null) } }
}
