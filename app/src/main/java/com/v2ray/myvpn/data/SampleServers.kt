package com.v2ray.myvpn.data

import com.v2ray.myvpn.model.Protocol
import com.v2ray.myvpn.model.ServerConfig

object SampleServers {

    val testServer =
        ServerConfig(
            id = "1",
            remarks = "Demo Server",
            address = "1.1.1.1",
            port = 443,
            uuid = "00000000-0000-0000-0000-000000000000",
            protocol = Protocol.VLESS,
            tls = true
        )
}
