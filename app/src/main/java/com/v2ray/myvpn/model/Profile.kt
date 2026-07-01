package com.v2ray.myvpn.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(

    val id: String,

    val name: String,

    val remark: String = "",

    val protocol: String,

    val host: String,

    val port: Int,

    val uuid: String,

    val security: String,

    val network: String,

    val sni: String = "",

    val path: String = "",

    val subscriptionId: String? = null,

    val enabled: Boolean = true,

    val selected: Boolean = false,

    val ping: Int = -1,

    val country: String = "Unknown"
)
