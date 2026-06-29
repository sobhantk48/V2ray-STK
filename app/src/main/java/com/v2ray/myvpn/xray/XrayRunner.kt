package com.v2ray.myvpn.xray

import android.content.Context
import android.util.Log
import java.io.File

object XrayRunner {

    private const val TAG = "V2raySTK"

    private var process: Process? = null

    fun isRunning(): Boolean {
        return process != null
    }

    fun start(
        context: Context
    ): Boolean {

        if (process != null) {
            return true
        }

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

        Log.d(
            TAG,
            "binary=${binary.absolutePath}"
        )

        Log.d(
            TAG,
            "config=${config.absolutePath}"
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

            binary.setExecutable(
                true,
                false
            )

            process =
                ProcessBuilder(
                    binary.absolutePath,
                    "-config",
                    config.absolutePath
                )
                    .redirectErrorStream(true)
                    .start()

            Thread {

                try {

                    process
                        ?.inputStream
                        ?.bufferedReader()
                        ?.forEachLine {

                            Log.d(
                                TAG,
                                "XRAY: $it"
                            )
                        }

                } catch (e: Exception) {

                    Log.e(
                        TAG,
                        "log reader error",
                        e
                    )
                }

            }.start()

            Log.d(
                TAG,
                "xray started pid=${process?.pid()}"
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

        process?.destroy()

        process = null

        Log.d(
            TAG,
            "xray stopped"
        )
    }
}
