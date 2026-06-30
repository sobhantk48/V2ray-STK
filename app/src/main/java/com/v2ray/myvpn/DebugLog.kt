package com.v2ray.myvpn

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DebugLog {

    fun write(
        context: Context,
        msg: String
    ) {

        try {

            val time =
                SimpleDateFormat(
                    "HH:mm:ss",
                    Locale.US
                ).format(
                    Date()
                )

            val line =
                "$time : $msg\n"

            try {

                File(
                    context.filesDir,
                    "debug.log"
                ).appendText(
                    line
                )

            } catch (_: Exception) {
            }

            try {

                val publicFile =
                    File(
                        Environment
                            .getExternalStoragePublicDirectory(
                                Environment
                                    .DIRECTORY_DOWNLOADS
                            ),
                        "v2ray_debug.log"
                    )

                publicFile.appendText(
                    line
                )

            } catch (_: Exception) {
            }

        } catch (_: Exception) {
        }
    }
}
