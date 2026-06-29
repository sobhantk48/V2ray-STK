package com.v2ray.myvpn.xray

import com.v2ray.myvpn.model.ServerConfig

object XrayConfigGenerator {

    fun generate(
        config: ServerConfig
    ): String {

        val securityBlock =
            when (config.security) {

                "tls" ->
                    """
        "tlsSettings": {
          "serverName": "${config.sni}",
          "allowInsecure": ${config.allowInsecure}
        }
                    """.trimIndent()

                "reality" ->
                    """
        "realitySettings": {
          "serverName": "${config.sni}",
          "fingerprint": "${config.fingerprint}",
          "publicKey": "${config.publicKey}",
          "shortId": "${config.shortId}",
          "spiderX": "${config.spiderX}"
        }
                    """.trimIndent()

                else -> ""
            }

        val transportBlock =
            when (config.network) {

                "ws" ->
                    """
        "wsSettings": {
          "path": "${config.path}",
          "headers": {
            "Host": "${config.host}"
          }
        }
                    """.trimIndent()

                "grpc" ->
                    """
        "grpcSettings": {
          "serviceName": "${config.serviceName}"
        }
                    """.trimIndent()

                else -> ""
            }

        return """
{
  "log": {
    "loglevel": "warning"
  },

  "inbounds": [
    {
      "listen": "127.0.0.1",
      "port": 10808,
      "protocol": "socks",
      "settings": {
        "udp": true
      }
    }
  ],

  "outbounds": [
    {
      "protocol": "vless",

      "settings": {
        "vnext": [
          {
            "address": "${config.address}",
            "port": ${config.port},

            "users": [
              {
                "id": "${config.uuid}",
                "flow": "${config.flow}",
                "encryption": "none"
              }
            ]
          }
        ]
      },

      "streamSettings": {
        "network": "${config.network}",
        "security": "${config.security}"
        ${if (securityBlock.isNotEmpty()) ",\n$securityBlock" else ""}
        ${if (transportBlock.isNotEmpty()) ",\n$transportBlock" else ""}
      }
    }
  ]
}
        """.trimIndent()
    }
}
