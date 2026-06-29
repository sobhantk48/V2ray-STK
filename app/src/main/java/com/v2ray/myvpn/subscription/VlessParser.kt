package com.v2ray.myvpn.subscription

import android.net.Uri
import com.v2ray.myvpn.model.ServerConfig

object VlessParser {

    fun parse(link: String): ServerConfig {
        require(link.startsWith("vless://")) {
            "Invalid VLESS link"
        }

        val uri = Uri.parse(link)

        return ServerConfig(
            remarks = uri.fragment ?: "VLESS",

            protocol = "vless",

            address = uri.host ?: "",

            port = uri.port,

            uuid = uri.userInfo ?: "",

            security =
                uri.getQueryParameter("security")
                    ?: "none",

            network =
                uri.getQueryParameter("type")
                    ?: "tcp",

            path =
                uri.getQueryParameter("path")
                    ?: "",

            host =
                uri.getQueryParameter("host")
                    ?: "",

            sni =
                uri.getQueryParameter("sni")
                    ?: "",

            allowInsecure =
                (uri.getQueryParameter(
                    "allowInsecure"
                ) ?: "0") == "1"
        )
    }
}
