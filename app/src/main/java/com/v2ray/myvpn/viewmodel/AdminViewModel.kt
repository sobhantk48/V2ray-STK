package com.v2ray.myvpn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.myvpn.security.AdminRepository
import com.v2ray.myvpn.security.AdminSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context =
        application.applicationContext

    companion object {
        private const val DEFAULT_ADMIN_PASSWORD = "1311"
    }

    private val _loading =
        MutableStateFlow(false)

    val loading: StateFlow<Boolean>
        get() = _loading

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?>
        get() = _error

    fun login(
        password: String
    ) {

        viewModelScope.launch {

            _loading.value = true
            _error.value = null

            try {

                if (
                    !AdminRepository.hasPassword(
                        context
                    )
                ) {

                    AdminRepository.savePassword(
                        context,
                        DEFAULT_ADMIN_PASSWORD
                    )
                }

                val ok =
                    AdminRepository.verifyPassword(
                        context,
                        password
                    )

                if (ok) {

                    AdminSession.login()

                } else {

                    _error.value =
                        "Wrong password"
                }

            } catch (_: Exception) {

                _error.value =
                    "Login failed"

            } finally {

                _loading.value = false
            }
        }
    }

    fun changePassword(
        password: String
    ) {

        AdminRepository.savePassword(
            context,
            password
        )
    }

    fun logout() {

        AdminSession.logout()
    }

    fun isLoggedIn(): Boolean {

        return AdminSession.isLoggedIn()
    }
}
