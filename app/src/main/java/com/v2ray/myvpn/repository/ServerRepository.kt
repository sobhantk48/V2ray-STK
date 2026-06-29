package com.v2ray.myvpn.repository

import com.v2ray.myvpn.model.ServerConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ServerRepository {

    private val _servers =
        MutableStateFlow<List<ServerConfig>>(emptyList())

    val servers: StateFlow<List<ServerConfig>>
        get() = _servers

    fun add(server: ServerConfig) {
        _servers.value =
            _servers.value + server
    }

    fun remove(id: String) {
        _servers.value =
            _servers.value.filterNot {
                it.id == id
            }
    }

    fun current(): ServerConfig? {
        return _servers.value.firstOrNull()
    }
}
