package com.v2ray.myvpn.security

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AdminSession {

    private val _loggedIn =
        MutableStateFlow(false)

    val loggedIn: StateFlow<Boolean>
        get() = _loggedIn

    fun login() {
        _loggedIn.value = true
    }

    fun logout() {
        _loggedIn.value = false
    }

    fun isLoggedIn(): Boolean {
        return _loggedIn.value
    }
}
