package com.v2ray.myvpn.security

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AdminSession {
    private const val DEFAULT_PASSWORD = "1311"
    private var currentPassword = DEFAULT_PASSWORD

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn

    fun validatePassword(input: String): Boolean = input == currentPassword

    fun login() { _loggedIn.value = true }
    fun logout() { _loggedIn.value = false }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        return if (oldPassword == currentPassword) {
            currentPassword = newPassword
            true
        } else {
            false
        }
    }
}
