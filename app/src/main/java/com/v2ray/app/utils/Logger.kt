package com.v2ray.app.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    private const val TAG = "V2RAY_STK"
    private const val LOG_FILE_NAME = "v2ray_stk_log.txt"
    private var logFile: File? = null
    private var isInitialized = false
    private lateinit var context: Context
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    fun initialize(ctx: Context) {
        context = ctx.applicationContext
        try {
            // استفاده از Documents برای Android 10+ و حافظه داخلی برای نسخه‌های پایین‌تر
            val externalDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            } else {
                Environment.getExternalStorageDirectory()
            }

            val appDir = File(externalDir, "V2RAY_STK_LOGS")
            if (!appDir.exists()) {
                val created = appDir.mkdirs()
                Log.d(TAG, "Log directory created: $created at ${appDir.absolutePath}")
            }

            logFile = File(appDir, LOG_FILE_NAME)
            if (!logFile!!.exists()) {
                logFile!!.createNewFile()
                Log.d(TAG, "Log file created at ${logFile!!.absolutePath}")
            }

            isInitialized = true
            writeLog("=== V2RAY STK LOG STARTED ===")
            writeLog("Device: ${Build.MANUFACTURER} ${Build.MODEL}")
            writeLog("Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            writeLog("Log file: ${logFile!!.absolutePath}")
            writeLog("================================")
        } catch (e: Exception) {
            Log.e(TAG, "Logger init failed", e)
        }
    }

    fun writeLog(message: String, tag: String = TAG) {
        try {
            if (!isInitialized || logFile == null) return
            val logEntry = "[${dateFormat.format(Date())}] [$tag] $message\n"
            logFile?.appendText(logEntry)
            Log.d(tag, message)
            if (logFile?.length() ?: 0 > 10 * 1024 * 1024) rotateLogFile()
        } catch (_: Exception) {
            Log.d(tag, message)
        }
    }

    fun writeError(message: String, throwable: Throwable? = null, tag: String = TAG) {
        val errorMsg = if (throwable != null) "$message: ${throwable.message}\n${throwable.stackTraceToString()}" else message
        writeLog("ERROR: $errorMsg", tag)
    }

    private fun rotateLogFile() {
        try {
            logFile?.let {
                val archive = File(it.parent, "v2ray_stk_log_${System.currentTimeMillis()}.txt")
                it.renameTo(archive)
                it.createNewFile()
                writeLog("Log file rotated")
            }
        } catch (_: Exception) {}
    }

    fun getLogFilePath(): String? {
        return try {
            logFile?.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun getLogContent(): String? {
        return try {
            val file = logFile
            if (file == null || !file.exists()) {
                Log.e(TAG, "Log file does not exist")
                return null
            }
            val content = file.readText()
            Log.d(TAG, "Log content length: ${content.length}")
            content
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read log file", e)
            null
        }
    }

    fun clearLogs() {
        try {
            logFile?.delete()
            logFile?.createNewFile()
            writeLog("Logs cleared")
        } catch (_: Exception) {}
    }
}
