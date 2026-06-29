package com.v2ray.myvpn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.myvpn.model.VpnState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _vpnState =
        MutableStateFlow(VpnState.DISCONNECTED)

    val vpnState: StateFlow<VpnState>
        get() = _vpnState

    fun toggle() {
        when (_vpnState.value) {

            VpnState.DISCONNECTED -> {
                connect()
            }

            VpnState.CONNECTED -> {
                disconnect()
            }

            else -> {}
        }
    }

    private fun connect() {
        viewModelScope.launch {
            _vpnState.value = VpnState.CONNECTING
            delay(1500)
            _vpnState.value = VpnState.CONNECTED
        }
    }

    private fun disconnect() {
        viewModelScope.launch {
            _vpnState.value = VpnState.DISCONNECTING
            delay(1000)
            _vpnState.value = VpnState.DISCONNECTED
        }
    }
}
