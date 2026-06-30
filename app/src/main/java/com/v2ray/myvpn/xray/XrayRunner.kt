package com.v2ray.myvpn.xray

import android.content.Context
import android.util.Log
import java.io.File

object XrayRunner {

    private const val TAG = "V2raySTK"

    private var process: Process? = null

    fun isRunning(): Boolean {
        return process?.isAlive == true
    }

    fun start(
        context: Context
    ): Boolean {

        if (isRunning()) {
            Log.d(
                TAG,
                "xray already running"
            )
            return true
        }

        process = null

        val binary =
            File(
                context.applicationInfo.nativeLibraryDir,
                "libxray.so"
            )

        val config =
            File(
                context.filesDir,
                "config.json"
            )

        Log.d(
            TAG,
            "xray binary = ${binary.absolutePath}"
        )

        if (!binary.exists()) {

            Log.e(
                TAG,
                "libxray.so not found"
            )

            return false
        }

        if (!config.exists()) {

            Log.e(
                TAG,
                "config.json not found"
            )

            return false
        }

        return try {

            process =
                ProcessBuilder(
                    binary.absolutePath,
                    "-config",
                    config.absolutePath
                )
                    .redirectErrorStream(true)
                    .start()

            Log.d(
                TAG,
                "xray started successfully"
            )

            true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "failed to start xray",
                e
            )

            process = null

            false
        }
    }

    fun stop() {

        try {

            process?.let {

                if (it.isAlive) {

                    it.destroy()

                    try {
                        it.waitFor()
                    } catch (_: Exception) {
                    }

                    if (it.isAlive) {
                        it.destroyForcibly()
                    }
                }
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "stop failed",
                e
            )

        } finally {

            process = null

            Log.d(
                TAG,
                "xray stopped"
            )
        }
    }
}
