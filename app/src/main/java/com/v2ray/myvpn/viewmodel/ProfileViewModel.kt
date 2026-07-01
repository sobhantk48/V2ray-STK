package com.v2ray.myvpn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.v2ray.myvpn.model.Profile
import com.v2ray.myvpn.repository.ProfileRepository
import java.util.UUID
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class ProfileViewModel : ViewModel() {

    companion object {

        private fun writeLog(text: String) {
            try {
                val file =
                    File(
                        "/storage/emulated/0/MyXrayVPN_profile.log"
                    )

                file.appendText(
                    "${System.currentTimeMillis()} : $text\n"
                )
            } catch (_: Exception) {
            }
        }
    }

    val profiles: StateFlow<List<Profile>>
        get() = ProfileRepository.profiles

    fun addProfile(
        name: String,
        protocol: String,
        host: String,
        port: Int,
        uuid: String,
        security: String,
        network: String,
        sni: String = "",
        path: String = ""
    ) {

        writeLog("addProfile called")

        val profile =
            Profile(
                id = UUID.randomUUID().toString(),
                name = name,
                protocol = protocol,
                host = host,
                port = port,
                uuid = uuid,
                security = security,
                network = network,
                sni = sni,
                path = path
            )

        writeLog(
            "created profile = ${profile.name}"
        )

        ProfileRepository.add(profile)

        writeLog(
            "repository count = ${ProfileRepository.count()}"
        )
    }

    fun updateProfile(
        profile: Profile
    ) {
        writeLog("update profile")
        ProfileRepository.update(profile)
    }

    fun deleteProfile(
        profileId: String
    ) {
        writeLog("delete profile")
        ProfileRepository.delete(profileId)
    }

    fun selectProfile(
        profileId: String
    ) {
        writeLog("select profile")
        ProfileRepository.select(profileId)
    }

    fun getSelected(): Profile? {
        return ProfileRepository.getSelected()
    }

    fun profileCount(): Int {
        return ProfileRepository.count()
    }

    fun clearProfiles() {
        writeLog("clear profiles")
        ProfileRepository.clear()
    }
}
