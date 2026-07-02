package com.v2ray.app.v2ray

import android.content.Context
import com.v2ray.app.utils.Logger
import libv2ray.Libv2ray
import libv2ray.V2RayPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class V2RayManager(private val context: Context) {
    private var v2rayPoint: V2RayPoint? = null
    private var running = false

    suspend fun startV2Ray(configJson: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (running) return@withContext Result.success(Unit)

            val configFile = File(context.filesDir, "v2ray_config.json")
            configFile.writeText(configJson)

            v2rayPoint = Libv2ray.newV2RayPoint(object : libv2ray.V2RayVPNServiceSupportsSet {
                override fun startV2RayPoint() {
                    Logger.writeLog("V2RayPoint started")
                }

                override fun stopV2RayPoint() {
                    Logger.writeLog("V2RayPoint stopped")
                }

                override fun getDeviceId(): String {
                    return android.provider.Settings.Secure.getString(
                        context.contentResolver,
                        android.provider.Settings.Secure.ANDROID_ID
                    ) ?: "unknown"
                }

                override fun getPackageName(): String = context.packageName
                override fun getFileDescriptor(): Int = 0
                override fun protectSocket(socket: Int): Boolean = true

                override fun getNotificationId(): Int = 1001
                override fun getNotificationIcon(): Int = android.R.drawable.ic_menu_share
                override fun getNotificationTitle(): String = "V2RAY STK"
                override fun getNotificationMessage(): String = "Connected"
                override fun getNotificationChannelName(): String = "V2Ray VPN"
                override fun getNotificationChannelDescription(): String = "V2Ray VPN Connection"
                override fun getNotificationImportance(): Int = android.app.NotificationManager.IMPORTANCE_LOW
                override fun getNotificationVibrate(): Boolean = false
                override fun getNotificationLight(): Int = 0
                override fun getNotificationColor(): Int = 0
                override fun getNotificationSound(): String? = null
                override fun getNotificationContentIntent(): android.app.PendingIntent? = null
                override fun getNotificationDeleteIntent(): android.app.PendingIntent? = null
                override fun getNotificationOngoing(): Boolean = true
                override fun getNotificationAutoCancel(): Boolean = false
                override fun getNotificationUsesChronometer(): Boolean = false
                override fun getNotificationWhen(): Long = System.currentTimeMillis()
                override fun getNotificationShowWhen(): Boolean = true
                override fun getNotificationDefaults(): Int = 0
                override fun getNotificationVisibility(): Int = android.app.Notification.VISIBILITY_PUBLIC
                override fun getNotificationGroup(): String? = null
                override fun getNotificationGroupSummary(): Boolean = false
                override fun getNotificationSortKey(): String? = null
                override fun getNotificationProgressMax(): Int = 0
                override fun getNotificationProgress(): Int = 0
                override fun getNotificationProgressIndeterminate(): Boolean = false
                override fun getNotificationSubText(): String? = null
                override fun getNotificationInfoText(): String? = null
                override fun getNotificationRemoteInputHistory(): Array<String>? = null
                override fun getNotificationExtras(): android.os.Bundle? = null
                override fun getNotificationActions(): Array<android.app.Notification.Action>? = null
                override fun getNotificationStyle(): android.app.Notification.Style? = null
                override fun getNotificationCategory(): String? = null
                override fun getNotificationTimeout(): Long = 0
                override fun getNotificationMessageCount(): Int = 0
                override fun getNotificationPersonList(): Array<android.app.Person>? = null
                override fun getNotificationLargeIcon(): android.graphics.Bitmap? = null
                override fun getNotificationTicker(): CharSequence? = null
                override fun getNotificationSoundUri(): android.net.Uri? = null
            })

            val testResult = Libv2ray.testConfig(configFile.absolutePath)
            if (testResult != 0) {
                return@withContext Result.failure(Exception("Config test failed with code: $testResult"))
            }

            v2rayPoint?.start()
            running = true
            Logger.writeLog("V2Ray started successfully with libv2ray AAR")

            Result.success(Unit)
        } catch (e: Exception) {
            Logger.writeError("V2Ray start failed", e)
            Result.failure(e)
        }
    }

    suspend fun stopV2Ray(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            v2rayPoint?.stop()
            v2rayPoint = null
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
            val stats = v2rayPoint?.getStats()
            V2RayStats(
                uplink = stats?.uplink ?: 0,
                downlink = stats?.downlink ?: 0,
                connectionCount = stats?.connectionCount ?: 0
            )
        } catch (e: Exception) {
            Logger.writeError("Get stats failed", e)
            V2RayStats(0, 0, 0)
        }
    }
}

data class V2RayStats(
    val uplink: Long,
    val downlink: Long,
    val connectionCount: Int
)
