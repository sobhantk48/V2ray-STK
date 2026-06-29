package com.v2ray.myvpn.model

data class ServerConfig(
    val remarks: String = "",

    val protocol: String,

    val address: String,
    val port: Int,

    val uuid: String = "",
    val password: String = "",

    val security: String = "none",
    val network: String = "tcp",

    val path: String = "",
    val host: String = "",

    val sni: String = "",
    val alpn: List<String> = emptyList(),

    val allowInsecure: Boolean = false
)
