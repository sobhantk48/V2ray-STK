package com.v2ray.myvpn.xray

import android.content.Context
import android.util.Log
import java.io.File

object AssetExtractor {

    private const val TAG = "V2raySTK"

    fun extractXray(
        context: Context
    ): Boolean {

        return try {

            val output =
                File(
                    context.filesDir,
                    "xray"
                )

            if (output.exists()) {
                output.setExecutable(true)
                return true
            }

            context.assets
                .open("xray")
                .use { input ->

                    output.outputStream()
                        .use { out ->
                            input.copyTo(out)
                        }
                }

            output.setExecutable(
                true,
                false
            )

            Log.d(
                TAG,
                "xray extracted: ${output.absolutePath}"
            )

            true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "extract failed",
                e
            )

            false
        }
    }
}
