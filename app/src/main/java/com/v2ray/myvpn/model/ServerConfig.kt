package com.v2ray.myvpn.model

data class ServerConfig(
    val id: String,
    val remarks: String,
    val address: String,
    val port: Int,
    val uuid: String,
    val security: String = "auto",
    val network: String = "tcp",
    val tls: Boolean = false,
    val protocol: Protocol = Protocol.VMESS
)

enum class Protocol {
    VMESS,
    VLESS,
    TROJAN,
    SHADOWSOCKS
}
