package com.v2ray.myvpn.core

import android.content.Context
import android.util.Log
import com.v2ray.myvpn.model.VpnState
import com.v2ray.myvpn.vpn.VpnManager
import com.v2ray.myvpn.xray.XrayRunner

object CoreManager {

    private const val TAG = "V2raySTK"

    fun start(
        context: Context
    ): Boolean {

        Log.d(
            TAG,
            "CoreManager.start()"
        )

        VpnManager.setConnecting()

        return try {

            val result =
                XrayRunner.start(
                    context
                )

            if (result) {

                VpnManager.setConnected()

            } else {

                VpnManager.setDisconnected()
            }

            result

        } catch (e: Exception) {

            Log.e(
                TAG,
                "core start failed",
                e
            )

            VpnManager.setDisconnected()

            false
        }
    }

    fun stop() {

        Log.d(
            TAG,
            "CoreManager.stop()"
        )

        VpnManager.setDisconnecting()

        try {

            XrayRunner.stop()

        } catch (_: Exception) {
        }

        VpnManager.setDisconnected()
    }

    fun state(): VpnState {
        return VpnManager.state.value
    }
}
