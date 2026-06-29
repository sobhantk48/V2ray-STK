package com.v2ray.myvpn.vpn

import com.v2ray.myvpn.model.VpnState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object VpnManager {

    private val _state =
        MutableStateFlow(VpnState.DISCONNECTED)

    val state: StateFlow<VpnState>
        get() = _state

    fun connect() {
        _state.value = VpnState.CONNECTING
    }

    fun connected() {
        _state.value = VpnState.CONNECTED
    }

    fun disconnect() {
        _state.value = VpnState.DISCONNECTED
    }
}
