package com.v2ray.myvpn.xray

import android.content.Context
import android.util.Log
import java.io.File

object XrayRunner {

    private const val TAG = "V2raySTK"

    private var process: Process? = null

    fun start(
        context: Context
    ): Boolean {

        try {

            val xray =
                File(
                    context.applicationInfo.nativeLibraryDir,
                    "libxray.so"
                )

            Log.d(
                TAG,
                "nativeLibraryDir = ${
                    context.applicationInfo.nativeLibraryDir
                }"
            )

            Log.d(
                TAG,
                "exists = ${xray.exists()}"
            )

            Log.d(
                TAG,
                "canExecute = ${xray.canExecute()}"
            )

            Log.d(
                TAG,
                "absolutePath = ${
                    xray.absolutePath
                }"
            )

            Log.d(
                TAG,
                "xray binary = ${
                    xray.absolutePath
                }"
            )

            if (!xray.exists()) {

                File(
                    context.applicationInfo.nativeLibraryDir
                ).listFiles()?.forEach {

                    Log.d(
                        TAG,
                        "native file: ${it.name}"
                    )
                }

                Log.e(
                    TAG,
                    "libxray.so not found"
                )

                return false
            }

            val config =
                File(
                    context.filesDir,
                    "config.json"
                )

            process =
                ProcessBuilder(
                    xray.absolutePath,
                    "run",
                    "-config",
                    config.absolutePath
                )
                    .redirectErrorStream(
                        true
                    )
                    .start()

            return true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "failed to start xray",
                e
            )

            return false
        }
    }

    fun stop() {

        process?.destroy()
        process = null
    }
}
