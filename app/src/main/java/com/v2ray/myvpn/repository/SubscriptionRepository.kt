package com.v2ray.myvpn.repository

import com.v2ray.myvpn.model.ServerConfig
import com.v2ray.myvpn.subscription.SubscriptionParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Base64

class SubscriptionRepository {

    suspend fun load(url: String): List<ServerConfig> =
        withContext(Dispatchers.IO) {

            val response =
                URL(url)
                    .readText()

            val decoded =
                try {
                    String(
                        Base64
                            .getDecoder()
                            .decode(response.trim())
                    )
                } catch (_: Exception) {
                    response
                }

            SubscriptionParser.parse(decoded)
        }
}
