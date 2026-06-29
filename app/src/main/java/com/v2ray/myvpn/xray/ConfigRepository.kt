package com.v2ray.myvpn.xray

import android.content.Context
import java.io.File

object ConfigRepository {

    private const val FILE_NAME = "config.json"

    private fun configFile(
        context: Context
    ): File {
        return File(
            context.filesDir,
            FILE_NAME
        )
    }

    fun save(
        context: Context,
        json: String
    ) {
        configFile(context)
            .writeText(json)
    }

    fun load(
        context: Context
    ): String? {

        val file =
            configFile(context)

        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }

    fun exists(
        context: Context
    ): Boolean {
        return configFile(context)
            .exists()
    }

    fun delete(
        context: Context
    ) {
        configFile(context)
            .delete()
    }
}
