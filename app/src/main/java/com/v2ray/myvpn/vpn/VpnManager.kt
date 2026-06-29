package com.v2ray.myvpn.vpn

import com.v2ray.myvpn.model.VpnState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object VpnManager {

    private val _state =
        MutableStateFlow(VpnState.DISCONNECTED)

    val state: StateFlow<VpnState>
        get() = _state

    fun setConnected() {
        _state.value = VpnState.CONNECTED
    }

    fun setDisconnected() {
        _state.value = VpnState.DISCONNECTED
    }

    fun setConnecting() {
        _state.value = VpnState.CONNECTING
    }

    fun setDisconnecting() {
        _state.value = VpnState.DISCONNECTING
    }
}
