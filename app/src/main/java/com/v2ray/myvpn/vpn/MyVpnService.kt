package com.v2ray.myvpn.vpn

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import com.v2ray.myvpn.DebugLog

class MyVpnService : VpnService() {

    companion object {
        private const val TAG = "V2raySTK"
    }

    private var vpnInterface:
        ParcelFileDescriptor? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        DebugLog.write(
            this,
            "vpn service entered"
        )

        if (vpnInterface == null) {

            VpnManager.setConnecting()

            try {

                DebugLog.write(
                    this,
                    "building vpn interface"
                )

                vpnInterface =
                    Builder()
                        .setSession(
                            "V2ray STK"
                        )
                        .addAddress(
                            "10.0.0.2",
                            24
                        )
                        .addRoute(
                            "0.0.0.0",
                            0
                        )
                        .establish()

                if (vpnInterface != null) {

                    DebugLog.write(
                        this,
                        "vpn established"
                    )

                    Log.d(
                        TAG,
                        "VPN established"
                    )

                    VpnManager.setConnected()

                } else {

                    DebugLog.write(
                        this,
                        "vpn establish returned null"
                    )

                    VpnManager.setDisconnected()
                }

            } catch (e: Exception) {

                DebugLog.write(
                    this,
                    "vpn exception: ${e.message}"
                )

                Log.e(
                    TAG,
                    "VPN error",
                    e
                )

                VpnManager.setDisconnected()
            }
        }

        return Service.START_STICKY
    }

    override fun onDestroy() {

        DebugLog.write(
            this,
            "vpn service destroyed"
        )

        vpnInterface?.close()
        vpnInterface = null

        VpnManager.setDisconnected()

        super.onDestroy()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {
        return super.onBind(intent)
    }
}
