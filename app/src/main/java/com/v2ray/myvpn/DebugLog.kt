package com.v2ray.myvpn

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object DebugLog {

    fun write(
        context: Context,
        msg: String
    ) {
        try {
            val file =
                File(
                    context.filesDir,
                    "debug.log"
                )

            val time =
                SimpleDateFormat(
                    "HH:mm:ss",
                    Locale.US
                ).format(Date())

            file.appendText(
                "$time : $msg\n"
            )
        } catch (_: Exception) {
        }
    }
}
