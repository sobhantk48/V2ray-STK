package com.v2ray.myvpn.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.subscription.VlessParser
import com.v2ray.myvpn.vpn.MyVpnService
import com.v2ray.myvpn.vpn.VpnManager
import com.v2ray.myvpn.xray.AssetExtractor
import com.v2ray.myvpn.xray.ConfigRepository
import com.v2ray.myvpn.xray.XrayConfigGenerator
import com.v2ray.myvpn.xray.XrayRunner
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

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

            val context =
                getApplication<Application>()

            val config =
                VlessParser.parse(
                    "vless://11111111-1111-1111-1111-111111111111@example.com:443?security=tls&type=tcp&sni=google.com#Test"
                )

            Log.d(TAG, "CONFIG = $config")

            val json =
                XrayConfigGenerator.generate(
                    config
                )

            ConfigRepository.save(
                context,
                json
            )

            if (
                !AssetExtractor.extractXray(
                    context
                )
            ) {
                Log.e(TAG, "xray extraction failed")
                return
            }

            if (
                XrayRunner.start(
                    context
                )
            ) {

                context.startService(
                    Intent(
                        context,
                        MyVpnService::class.java
                    )
                )

                VpnManager.setConnected()

            } else {

                VpnManager.setDisconnected()
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "connect failed",
                e
            )

            VpnManager.setDisconnected()
        }
    }

    private fun disconnect() {

        val context =
            getApplication<Application>()

        XrayRunner.stop()

        context.stopService(
            Intent(
                context,
                MyVpnService::class.java
            )
        )

        VpnManager.setDisconnecting()
        VpnManager.setDisconnected()
    }
}
