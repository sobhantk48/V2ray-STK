package com.v2ray.myvpn.model

data class Subscription(

    val id: String,

    val name: String,

    val url: String,

    val enabled: Boolean = true,

    val profilesCount: Int = 0,

    val lastUpdate: Long = 0L
)
