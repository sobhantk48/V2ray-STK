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

            // استفاده از cacheDir برای کپی فایل باینری (دسترسی نوشتن آسان‌تر)
            val cacheDir = context.cacheDir
            val assetsDir = File(cacheDir, "assets")
            if (!assetsDir.exists()) assetsDir.mkdirs()

            // کپی فایل‌ها از assets برنامه به cacheDir
            copyAssetToFile("xray", File(assetsDir, "xray"))
            copyAssetToFile("geoip.dat", File(assetsDir, "geoip.dat"))
            copyAssetToFile("geosite.dat", File(assetsDir, "geosite.dat"))

            val xrayFile = File(assetsDir, "xray")
            if (!xrayFile.exists()) {
                return@withContext Result.failure(Exception("Xray binary not found in cache"))
            }

            // تنظیم مجوزهای کامل
            try {
                val chmodProcess = Runtime.getRuntime().exec(arrayOf("chmod", "777", xrayFile.absolutePath))
                chmodProcess.waitFor()
                Logger.writeLog("chmod 777 executed for ${xrayFile.absolutePath}")
            } catch (e: Exception) {
                Logger.writeError("chmod failed", e)
                xrayFile.setExecutable(true, false)
            }

            // ذخیره کانفیگ در cacheDir
            val configFile = File(cacheDir, "v2ray_config.json")
            configFile.writeText(configJson)

            // ساخت دستور با sh -c (برای اطمینان از اجرا)
            val command = arrayOf(
                "sh",
                "-c",
                "${xrayFile.absolutePath} run -config ${configFile.absolutePath} -format json"
            )

            Logger.writeLog("Starting Xray with command: ${command.joinToString(" ")}")

            val processBuilder = ProcessBuilder(*command)
            processBuilder.redirectErrorStream(true)
            processBuilder.directory(cacheDir)
            // تنظیم PATH
            processBuilder.environment()["PATH"] = "${System.getenv("PATH")}:${cacheDir.absolutePath}/assets"

            process = processBuilder.start()

            // خواندن خروجی
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
            Logger.writeLog("Xray started successfully from cache")
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
