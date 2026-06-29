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
                context.filesDir,
                "xray"
            )

        val config =
            File(
                context.filesDir,
                "config.json"
            )

        if (!binary.exists()) {
            Log.e(
                TAG,
                "xray binary not found"
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

            binary.setExecutable(true)

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
                "xray started"
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

                    Log.d(
                        TAG,
                        "destroying xray process"
                    )

                    it.destroy()

                    try {
                        it.waitFor()
                    } catch (_: Exception) {
                    }

                    if (it.isAlive) {

                        Log.d(
                            TAG,
                            "forcing xray process"
                        )

                        it.destroyForcibly()

                        try {
                            it.waitFor()
                        } catch (_: Exception) {
                        }
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
