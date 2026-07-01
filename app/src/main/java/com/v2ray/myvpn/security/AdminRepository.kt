package com.v2ray.myvpn.security

import android.content.Context

object AdminRepository {

    private const val PREF =
        "admin_security"

    private const val KEY_PASSWORD =
        "admin_password"

    fun savePassword(
        context: Context,
        password: String
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_PASSWORD,
                password
            )
            .apply()
    }

    fun verifyPassword(
        context: Context,
        password: String
    ): Boolean {

        val saved =
            context
                .getSharedPreferences(
                    PREF,
                    Context.MODE_PRIVATE
                )
                .getString(
                    KEY_PASSWORD,
                    null
                )

        return saved == password
    }

    fun hasPassword(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .contains(
                KEY_PASSWORD
            )
    }

    fun clearPassword(
        context: Context
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .remove(
                KEY_PASSWORD
            )
            .apply()
    }
}
