package com.v2ray.myvpn.repository

import com.v2ray.myvpn.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ProfileRepository {

    private val _profiles =
        MutableStateFlow<List<Profile>>(
            emptyList()
        )

    val profiles: StateFlow<List<Profile>>
        get() = _profiles

    fun getAll(): List<Profile> {
        return _profiles.value
    }

    fun getSelected(): Profile? {
        return _profiles.value
            .firstOrNull {
                it.selected
            }
    }

    fun add(
        profile: Profile
    ) {

        _profiles.update {
            it + profile
        }
    }

    fun update(
        profile: Profile
    ) {

        _profiles.update { list ->

            list.map {

                if (
                    it.id == profile.id
                ) {
                    profile
                } else {
                    it
                }
            }
        }
    }

    fun delete(
        profileId: String
    ) {

        _profiles.update {

            it.filterNot { profile ->
                profile.id == profileId
            }
        }
    }

    fun select(
        profileId: String
    ) {

        _profiles.update { list ->

            list.map { profile ->

                profile.copy(
                    selected =
                        profile.id ==
                        profileId
                )
            }
        }
    }

    fun clear() {

        _profiles.value =
            emptyList()
    }

    fun count(): Int {

        return _profiles
            .value
            .size
    }
}
