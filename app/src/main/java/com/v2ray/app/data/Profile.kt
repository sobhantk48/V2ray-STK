package com.v2ray.app.data

import android.util.Base64
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

@Serializable
data class Profile(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val type: String,
    val address: String,
    val port: Int,
    val uuid: String = "",
    val security: String = "auto",
    val encryption: String = "none",
    val flow: String = "",
    val sni: String = "",
    val fingerprint: String = "chrome",
    val realityPublicKey: String = "",
    val realityShortId: String = "",
    val selected: Boolean = false,
    val ping: Int = 0,
    val country: String = "",
    val city: String = ""
) {
    fun toV2RayConfig(): String {
        return when (type.uppercase()) {
            "VLESS" -> buildVlessConfig()
            "VMESS" -> buildVmessConfig()
            "TROJAN" -> buildTrojanConfig()
            "SHADOWSOCKS" -> buildShadowsocksConfig()
            else -> buildVlessConfig()
        }
    }

    private fun buildVlessConfig(): String {
        return buildJsonObject {
            put("v", "2")
            put("ps", name)
            put("add", address)
            put("port", port)
            put("id", uuid.ifEmpty { "00000000-0000-0000-0000-000000000000" })
            put("aid", "0")
            put("net", "tcp")
            put("type", "none")
            put("host", "")
            put("path", "")
            put("tls", if (sni.isNotBlank() || realityPublicKey.isNotBlank()) "tls" else "")
            put("sni", sni.ifEmpty { address })
            put("fp", fingerprint)
            if (flow.isNotBlank()) put("flow", flow)
            if (realityPublicKey.isNotBlank()) {
                put("pbk", realityPublicKey)
                put("sid", realityShortId)
            }
        }.toString()
    }

    private fun buildVmessConfig(): String {
        return buildJsonObject {
            put("v", "2")
            put("ps", name)
            put("add", address)
            put("port", port)
            put("id", uuid.ifEmpty { "00000000-0000-0000-0000-000000000000" })
            put("aid", "0")
            put("net", "tcp")
            put("type", "none")
            put("host", "")
            put("path", "")
            put("tls", if (sni.isNotBlank()) "tls" else "")
        }.toString()
    }

    private fun buildTrojanConfig(): String {
        return buildJsonObject {
            put("name", name)
            put("type", "trojan")
            put("server", address)
            put("port", port)
            put("password", uuid.ifEmpty { "password" })
            put("sni", sni.ifEmpty { address })
            put("skip-cert-verify", true)
            put("udp", true)
        }.toString()
    }

    private fun buildShadowsocksConfig(): String {
        return buildJsonObject {
            put("name", name)
            put("type", "shadowsocks")
            put("server", address)
            put("port", port)
            put("password", uuid.ifEmpty { "password" })
            put("method", encryption.ifEmpty { "chacha20-ietf-poly1305" })
        }.toString()
    }

    companion object {
        fun fromLink(link: String): Profile? {
            return try {
                when {
                    link.startsWith("vless://") -> parseVless(link)
                    link.startsWith("vmess://") -> parseVmess(link)
                    link.startsWith("trojan://") -> parseTrojan(link)
                    link.startsWith("ss://") -> parseShadowsocks(link)
                    else -> null
                }
            } catch (_: Exception) { null }
        }

        private fun parseVless(link: String): Profile {
            val parts = link.substringAfter("vless://").split("@")
            val uuid = parts[0]
            val rest = parts[1].split("?")
            val addressPort = rest[0].split(":")
            val address = addressPort[0]
            val port = addressPort[1].toIntOrNull() ?: 443
            val params = rest.getOrNull(1)?.split("&")?.associate {
                val kv = it.split("=")
                kv[0] to kv.getOrNull(1).orEmpty()
            } ?: emptyMap()

            return Profile(
                name = params["ps"] ?: address,
                type = "VLESS",
                address = address,
                port = port,
                uuid = uuid,
                sni = params["sni"] ?: "",
                fingerprint = params["fp"] ?: "chrome",
                flow = params["flow"] ?: "",
                realityPublicKey = params["pbk"] ?: "",
                realityShortId = params["sid"] ?: ""
            )
        }

        private fun parseVmess(link: String): Profile {
            val json = String(Base64.decode(link.substringAfter("vmess://"), Base64.DEFAULT))
            val obj = Json.parseToJsonElement(json).jsonObject
            return Profile(
                name = obj["ps"]?.jsonPrimitive?.content ?: "VMESS",
                type = "VMESS",
                address = obj["add"]?.jsonPrimitive?.content ?: "",
                port = obj["port"]?.jsonPrimitive?.content?.toIntOrNull() ?: 443,
                uuid = obj["id"]?.jsonPrimitive?.content ?: "",
                sni = obj["sni"]?.jsonPrimitive?.content ?: "",
                encryption = obj["method"]?.jsonPrimitive?.content ?: "auto"
            )
        }

        private fun parseTrojan(link: String): Profile {
            val parts = link.substringAfter("trojan://").split("@")
            val password = parts[0]
            val addressPort = parts[1].split(":")[0].split("?")
            val address = addressPort[0]
            val port = addressPort.getOrNull(1)?.toIntOrNull() ?: 443
            return Profile(
                name = "Trojan",
                type = "TROJAN",
                address = address,
                port = port,
                uuid = password
            )
        }

        private fun parseShadowsocks(link: String): Profile {
            val parts = link.substringAfter("ss://").split("@")
            val methodPassword = String(Base64.decode(parts[0], Base64.DEFAULT)).split(":")
            val method = methodPassword[0]
            val password = methodPassword[1]
            val addressPort = parts[1].split(":")[0].split("?")
            val address = addressPort[0]
            val port = addressPort.getOrNull(1)?.toIntOrNull() ?: 443
            return Profile(
                name = "Shadowsocks",
                type = "SHADOWSOCKS",
                address = address,
                port = port,
                uuid = password,
                encryption = method
            )
        }
    }
}
