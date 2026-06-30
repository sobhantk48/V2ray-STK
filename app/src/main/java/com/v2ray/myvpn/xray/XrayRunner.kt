package com.v2ray.myvpn.xray

import android.content.Context
import android.util.Log
import com.v2ray.myvpn.DebugLog
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

            DebugLog.write(
                context,
                "xray binary = ${binary.absolutePath}"
            )

            DebugLog.write(
                context,
                "config file = ${config.absolutePath}"
            )

            if (!binary.exists()) {

                DebugLog.write(
                    context,
                    "libxray.so not found"
                )

                return false
            }

            process =
                ProcessBuilder(
                    binary.absolutePath,
                    "-config",
                    config.absolutePath
                )
                    .redirectErrorStream(
                        true
                    )
                    .start()

            Thread {

                try {

                    process
                        ?.inputStream
                        ?.bufferedReader()
                        ?.forEachLine {

                            Log.d(
                                TAG,
                                it
                            )

                            DebugLog.write(
                                context,
                                "XRAY: $it"
                            )
                        }

                } catch (e: Exception) {

                    DebugLog.write(
                        context,
                        "reader exception: ${e.message}"
                    )
                }

            }.start()

            DebugLog.write(
                context,
                "xray process started"
            )

            true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "xray start failed",
                e
            )

            DebugLog.write(
                context,
                "start exception: ${e.message}"
            )

            false
        }
    }

    fun stop() {

        try {

            process?.destroy()

        } catch (_: Exception) {
        }

        process = null
    }
}
