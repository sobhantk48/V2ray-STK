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

        return try {

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

            Log.d(
                TAG,
                "config file = ${config.absolutePath}"
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

                } catch (_: Exception) {
                }

            }.start()

            true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "xray start exception",
                e
            )

            false
        }
    }

    fun stop() {

        process?.destroy()
        process = null
    }
}
