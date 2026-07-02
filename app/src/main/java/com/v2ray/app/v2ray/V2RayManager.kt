package com.v2ray.app.v2ray

import android.content.Context
import com.v2ray.app.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class V2RayManager(private val context: Context) {
    private var process: Process? = null
    private var running = false

    suspend fun startV2Ray(configJson: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (running) return@withContext Result.success(Unit)

            // مسیر /data/local/tmp
            val tmpDir = File("/data/local/tmp")
            if (!tmpDir.exists()) {
                return@withContext Result.failure(Exception("/data/local/tmp not found"))
            }

            // کپی فایل‌ها به /data/local/tmp
            copyAssetToFile("xray", File(tmpDir, "xray"))
            copyAssetToFile("geoip.dat", File(tmpDir, "geoip.dat"))
            copyAssetToFile("geosite.dat", File(tmpDir, "geosite.dat"))

            val xrayFile = File(tmpDir, "xray")
            if (!xrayFile.exists()) {
                return@withContext Result.failure(Exception("Xray binary not found in /data/local/tmp"))
            }

            // تنظیم مجوز اجرا
            try {
                val chmodProcess = Runtime.getRuntime().exec(arrayOf("chmod", "777", xrayFile.absolutePath))
                chmodProcess.waitFor()
                Logger.writeLog("chmod 777 executed for ${xrayFile.absolutePath}")
            } catch (e: Exception) {
                Logger.writeError("chmod failed", e)
                xrayFile.setExecutable(true, false)
            }

            // ذخیره کانفیگ در /data/local/tmp
            val configFile = File(tmpDir, "v2ray_config.json")
            configFile.writeText(configJson)

            // ساخت دستور اجرا با sh -c
            val command = arrayOf(
                "sh",
                "-c",
                "${xrayFile.absolutePath} run -config ${configFile.absolutePath} -format json"
            )

            Logger.writeLog("Starting Xray with command: ${command.joinToString(" ")}")

            val processBuilder = ProcessBuilder(*command)
            processBuilder.redirectErrorStream(true)
            processBuilder.directory(tmpDir)
            // تنظیم PATH برای دسترسی به فایل‌های دیتابیس
            processBuilder.environment()["PATH"] = "${System.getenv("PATH")}:${tmpDir.absolutePath}"

            process = processBuilder.start()

            // خواندن خروجی استاندارد
            Thread {
                try {
                    val reader = process?.inputStream?.bufferedReader()
                    reader?.forEachLine { line ->
                        Logger.writeLog("[Xray] $line")
                    }
                } catch (_: Exception) { }
            }.start()

            // خواندن خطاها
            Thread {
                try {
                    val reader = process?.errorStream?.bufferedReader()
                    reader?.forEachLine { line ->
                        Logger.writeLog("[Xray-ERR] $line")
                    }
                } catch (_: Exception) { }
            }.start()

            running = true
            Logger.writeLog("Xray started successfully from /data/local/tmp")
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.writeError("Xray start failed", e)
            Result.failure(e)
        }
    }

    private fun copyAssetToFile(assetName: String, targetFile: File) {
        try {
            if (targetFile.exists()) return
            context.assets.open(assetName).use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }
            Logger.writeLog("Copied asset: $assetName to ${targetFile.absolutePath}")
        } catch (e: Exception) {
            Logger.writeError("Failed to copy asset: $assetName", e)
        }
    }

    suspend fun stopV2Ray(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            process?.destroy()
            process?.waitFor()
            process = null
            running = false
            Logger.writeLog("Xray stopped")
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.writeError("Xray stop failed", e)
            Result.failure(e)
        }
    }

    fun isRunning(): Boolean = running

    fun getStats(): V2RayStats {
        return V2RayStats(0, 0, 0)
    }
}

data class V2RayStats(
    val uplink: Long,
    val downlink: Long,
    val connectionCount: Int
)
