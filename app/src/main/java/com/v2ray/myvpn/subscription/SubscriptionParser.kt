package com.v2ray.myvpn.subscription

import com.v2ray.myvpn.model.ServerConfig

object SubscriptionParser {

    fun parse(content: String): List<ServerConfig> {
        return content
            .lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { line ->
                try {
                    when {
                        line.startsWith("vless://") ->
                            VlessParser.parse(line)

                        else -> null
                    }
                } catch (_: Exception) {
                    null
                }
            }
    }
}
