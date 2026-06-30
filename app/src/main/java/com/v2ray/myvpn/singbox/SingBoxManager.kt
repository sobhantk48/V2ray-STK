package com.v2ray.myvpn.singbox

import android.content.Context
import android.util.Log

object SingBoxManager {

    private const val TAG =
        "V2raySTK"

    private var running =
        false

    fun start(
        context: Context
    ): Boolean {

        Log.d(
            TAG,
            "SingBoxManager.start()"
        )

        running = true

        return true
    }

    fun stop() {

        Log.d(
            TAG,
            "SingBoxManager.stop()"
        )

        running = false
    }

    fun isRunning(): Boolean {
        return running
    }
}
