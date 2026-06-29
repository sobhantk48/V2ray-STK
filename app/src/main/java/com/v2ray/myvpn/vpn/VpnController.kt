package com.v2ray.myvpn.vpn

import android.content.Context
import android.content.Intent
import android.net.VpnService

object VpnController {

    fun prepare(
        context: Context
    ): Intent? {
        return VpnService.prepare(context)
    }

    fun start(
        context: Context
    ) {
        val intent =
            Intent(
                context,
                MyVpnService::class.java
            )

        context.startService(intent)
    }

    fun stop(
        context: Context
    ) {
        val intent =
            Intent(
                context,
                MyVpnService::class.java
            )

        context.stopService(intent)
    }
}
