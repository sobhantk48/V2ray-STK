package com.v2ray.myvpn.service

import android.content.Intent
import android.net.VpnService
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import com.v2ray.myvpn.DebugLog
import com.v2ray.myvpn.core.CoreManager

class VpnCoreService : VpnService() {

    companion object {

        private const val TAG =
            "V2raySTK"

        const val ACTION_START =
            "com.v2ray.myvpn.START"

        const val ACTION_STOP =
            "com.v2ray.myvpn.STOP"
    }

    private var vpn:
        ParcelFileDescriptor? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {

            ACTION_STOP -> {

                DebugLog.write(
                    this,
                    "VpnCoreService stop"
                )

                stopCore()

                stopSelf()

                return START_NOT_STICKY
            }

            else -> {

                DebugLog.write(
                    this,
                    "VpnCoreService start"
                )

                startCore()
            }
        }

        return START_STICKY
    }

    private fun startCore() {

        try {

            if (vpn == null) {

                DebugLog.write(
                    this,
                    "creating tun"
                )

                vpn =
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

                if (vpn == null) {

                    DebugLog.write(
                        this,
                        "tun create failed"
                    )

                    return
                }

                DebugLog.write(
                    this,
                    "tun created"
                )
            }

            CoreManager.start(
                this
            )

        } catch (e: Exception) {

            DebugLog.write(
                this,
                "core exception: ${e.message}"
            )

            Log.e(
                TAG,
                "core error",
                e
            )
        }
    }

    private fun stopCore() {

        try {

            CoreManager.stop()

        } catch (_: Exception) {
        }

        try {

            vpn?.close()

        } catch (_: Exception) {
        }

        vpn = null
    }

    override fun onDestroy() {

        stopCore()

        super.onDestroy()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {

        return super.onBind(
            intent
        )
    }
}
