package com.v2ray.myvpn.vpn

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.IBinder
import android.os.ParcelFileDescriptor

class MyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        if (vpnInterface == null) {
            vpnInterface =
                Builder()
                    .setSession("V2ray STK")
                    .addAddress(
                        "10.0.0.2",
                        24
                    )
                    .addRoute(
                        "0.0.0.0",
                        0
                    )
                    .establish()
        }

        return Service.START_STICKY
    }

    override fun onDestroy() {
        vpnInterface?.close()
        vpnInterface = null
        super.onDestroy()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {
        return super.onBind(intent)
    }
}
