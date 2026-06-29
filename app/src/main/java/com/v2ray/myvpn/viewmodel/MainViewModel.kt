package com.v2ray.myvpn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.subscription.VlessParser
import com.v2ray.myvpn.vpn.VpnManager
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    companion object {
        private const val TAG = "V2raySTK"
    }

    val vpnState: StateFlow<VpnState>
        get() = VpnManager.state

    fun toggle() {
        when (vpnState.value) {

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

        try {
            val config =
                VlessParser.parse(
                    "vless://11111111-1111-1111-1111-111111111111@example.com:443?security=tls&type=tcp&sni=google.com#Test"
                )

            Log.d(TAG, "CONFIG = $config")
        } catch (e: Exception) {
            Log.e(TAG, "Parser error", e)
        }

        VpnManager.setConnecting()
    }

    private fun disconnect() {
        VpnManager.setDisconnecting()
        VpnManager.setDisconnected()
    }
}
