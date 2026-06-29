package com.v2ray.myvpn.model

data class ServerConfig(

    // display
    val remarks: String = "",

    // protocol
    val protocol: String,

    // server
    val address: String,
    val port: Int,

    // auth
    val uuid: String = "",
    val password: String = "",
    val flow: String = "",

    // transport
    val network: String = "tcp",
    val host: String = "",
    val path: String = "",
    val serviceName: String = "",

    // tls/reality
    val security: String = "none",
    val sni: String = "",
    val fingerprint: String = "",
    val publicKey: String = "",
    val shortId: String = "",
    val spiderX: String = "",

    // tls options
    val alpn: List<String> = emptyList(),
    val allowInsecure: Boolean = false
)
