package com.v2ray.myvpn.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.v2ray.myvpn.DebugLog
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

        Log.d(TAG, "toggle clicked")

        when (vpnState.value) {

            VpnState.DISCONNECTED ->
                connect()

            VpnState.CONNECTED ->
                disconnect()

            else -> {}
        }
    }

    private fun connect() {

        val context =
            getApplication<Application>()

        DebugLog.write(
            context,
            "connect entered"
        )

        try {

            val config =
                VlessParser.parse(
                    "vless://11111111-1111-1111-1111-111111111111@example.com:443?security=tls&type=tcp&sni=google.com#Test"
                )

            DebugLog.write(
                context,
                "config parsed"
            )

            val json =
                XrayConfigGenerator.generate(
                    config
                )

            ConfigRepository.save(
                context,
                json
            )

            DebugLog.write(
                context,
                "config saved"
            )

            if (
                !AssetExtractor.extractXray(
                    context
                )
            ) {

                DebugLog.write(
                    context,
                    "xray extraction failed"
                )

                return
            }

            DebugLog.write(
                context,
                "xray extracted"
            )

            if (
                XrayRunner.start(
                    context
                )
            ) {

                DebugLog.write(
                    context,
                    "xray started"
                )

                context.startService(
                    Intent(
                        context,
                        MyVpnService::class.java
                    )
                )

                DebugLog.write(
                    context,
                    "vpn service start requested"
                )

                VpnManager.setConnected()

            } else {

                DebugLog.write(
                    context,
                    "xray start failed"
                )

                VpnManager.setDisconnected()
            }

        } catch (e: Exception) {

            DebugLog.write(
                context,
                "connect exception: ${e.message}"
            )

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

        DebugLog.write(
            context,
            "disconnect entered"
        )

        XrayRunner.stop()

        context.stopService(
            Intent(
                context,
                MyVpnService::class.java
            )
        )

        DebugLog.write(
            context,
            "vpn stopped"
        )

        VpnManager.setDisconnecting()
        VpnManager.setDisconnected()
    }
}
