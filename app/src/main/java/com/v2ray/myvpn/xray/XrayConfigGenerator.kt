package com.v2ray.myvpn.xray

import com.v2ray.myvpn.model.ServerConfig

object XrayConfigGenerator {

    fun generate(
        config: ServerConfig
    ): String {

        return """
        {
          "log": {
            "loglevel": "warning"
          },
          "inbounds": [
            {
              "port": 10808,
              "listen": "127.0.0.1",
              "protocol": "socks",
              "settings": {
                "udp": true
              }
            }
          ],
          "outbounds": [
            {
              "protocol": "${config.protocol}",
              "settings": {}
            }
          ]
        }
        """.trimIndent()
    }
}
