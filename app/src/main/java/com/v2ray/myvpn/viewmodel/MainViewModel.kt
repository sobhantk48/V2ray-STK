package com.v2ray.myvpn.viewmodel

import androidx.lifecycle.ViewModel
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.vpn.VpnManager
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    val vpnState: StateFlow<VpnState>
        get() = VpnManager.state

    fun toggle() {
        when (vpnState.value) {
            VpnState.DISCONNECTED -> {
                VpnManager.connect()
            }

            VpnState.CONNECTING -> {
            }

            VpnState.CONNECTED -> {
                VpnManager.disconnect()
            }

            VpnState.DISCONNECTING -> {
            }
        }
    }
}
