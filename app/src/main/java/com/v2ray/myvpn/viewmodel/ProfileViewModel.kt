package com.v2ray.myvpn.viewmodel

import androidx.lifecycle.ViewModel
import com.v2ray.myvpn.model.Profile
import com.v2ray.myvpn.repository.ProfileRepository
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class ProfileViewModel : ViewModel() {

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

        ProfileRepository.add(
            profile
        )
    }

    fun updateProfile(
        profile: Profile
    ) {

        ProfileRepository.update(
            profile
        )
    }

    fun deleteProfile(
        profileId: String
    ) {

        ProfileRepository.delete(
            profileId
        )
    }

    fun selectProfile(
        profileId: String
    ) {

        ProfileRepository.select(
            profileId
        )
    }

    fun getSelected(): Profile? {

        return ProfileRepository
            .getSelected()
    }

    fun profileCount(): Int {

        return ProfileRepository
            .count()
    }

    fun clearProfiles() {

        ProfileRepository.clear()
    }
}
