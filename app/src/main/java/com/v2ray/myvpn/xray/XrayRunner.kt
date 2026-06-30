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
                    context.filesDir,
                    "xray"
                )

            if (!binary.exists()) {

                DebugLog.write(
                    context,
                    "copying xray binary"
                )

                context.assets
                    .open("xray")
                    .use { input ->

                        binary.outputStream()
                            .use { output ->

                                input.copyTo(
                                    output
                                )
                            }
                    }

                binary.setExecutable(
                    true
                )
            }

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

        process?.destroy()
        process = null
    }
}
