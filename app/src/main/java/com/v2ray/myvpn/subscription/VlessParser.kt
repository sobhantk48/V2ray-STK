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

            // display
            remarks =
                uri.fragment ?: "VLESS",

            // protocol
            protocol = "vless",

            // server
            address =
                uri.host ?: "",

            port =
                uri.port,

            // auth
            uuid =
                uri.userInfo ?: "",

            flow =
                uri.getQueryParameter("flow")
                    ?: "",

            // transport
            network =
                uri.getQueryParameter("type")
                    ?: "tcp",

            host =
                uri.getQueryParameter("host")
                    ?: "",

            path =
                uri.getQueryParameter("path")
                    ?: "",

            serviceName =
                uri.getQueryParameter(
                    "serviceName"
                ) ?: "",

            // tls/reality
            security =
                uri.getQueryParameter(
                    "security"
                ) ?: "none",

            sni =
                uri.getQueryParameter("sni")
                    ?: "",

            fingerprint =
                uri.getQueryParameter("fp")
                    ?: "",

            publicKey =
                uri.getQueryParameter("pbk")
                    ?: "",

            shortId =
                uri.getQueryParameter("sid")
                    ?: "",

            spiderX =
                uri.getQueryParameter("spx")
                    ?: "",

            // tls options
            alpn =
                uri.getQueryParameter("alpn")
                    ?.split(",")
                    ?.filter {
                        it.isNotBlank()
                    }
                    ?: emptyList(),

            allowInsecure =
                (uri.getQueryParameter(
                    "allowInsecure"
                ) ?: "0") == "1"
        )
    }
}
