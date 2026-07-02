package com.v2ray.app.v2ray

import android.content.Context
import com.v2ray.app.utils.Logger
import com.v2ray.v2ray.V2Ray
import com.v2ray.v2ray.V2RayConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class V2RayManager(private val context: Context) {
    private var instance: V2Ray? = null
    private var running = false

    suspend fun startV2Ray(configJson: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (running) return@withContext Result.success(Unit)
            val configFile = File(context.filesDir, "v2ray_config.json").apply { writeText(configJson) }
            val config = V2RayConfig.fromFile(configFile)
            val v2ray = V2Ray(context, config)
            File(context.filesDir, "assets").mkdirs()
            v2ray.start()
            instance = v2ray
            running = true
            Logger.writeLog("V2Ray started successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.writeError("V2Ray start failed", e)
            Result.failure(e)
        }
    }

    suspend fun stopV2Ray(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            instance?.stop()
            instance = null
            running = false
            Logger.writeLog("V2Ray stopped")
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.writeError("V2Ray stop failed", e)
            Result.failure(e)
        }
    }

    fun isRunning(): Boolean = running

    fun getStats(): V2RayStats {
        return try {
            val stats = instance?.getStats()
            V2RayStats(stats?.uplink ?: 0, stats?.downlink ?: 0, stats?.connectionCount ?: 0)
        } catch (e: Exception) {
            Logger.writeError("Get stats failed", e)
            V2RayStats(0, 0, 0)
        }
    }
}

data class V2RayStats(val uplink: Long, val downlink: Long, val connectionCount: Int)
