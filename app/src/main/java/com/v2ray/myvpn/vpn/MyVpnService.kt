package com.v2ray.myvpn.vpn

import android.net.VpnService
import android.os.IBinder

class MyVpnService : VpnService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: android.content.Intent): IBinder? {
        return super.onBind(intent)
    }
}
