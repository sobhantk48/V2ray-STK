package com.v2ray.myvpn.repository

import com.v2ray.myvpn.model.Subscription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

object SubscriptionRepository {

    private val _subscriptions =
        MutableStateFlow<List<Subscription>>(
            emptyList()
        )

    val subscriptions:
        StateFlow<List<Subscription>>
        get() = _subscriptions

    fun add(
        name: String,
        url: String
    ) {

        val item =
            Subscription(
                id = UUID.randomUUID().toString(),
                name = name,
                url = url
            )

        _subscriptions.value =
            _subscriptions.value + item
    }

    fun remove(
        id: String
    ) {

        _subscriptions.value =
            _subscriptions.value.filter {
                it.id != id
            }
    }

    fun update(
        subscription: Subscription
    ) {

        _subscriptions.value =
            _subscriptions.value.map {

                if (
                    it.id ==
                    subscription.id
                ) {
                    subscription
                } else {
                    it
                }
            }
    }

    fun enable(
        id: String,
        enabled: Boolean
    ) {

        _subscriptions.value =
            _subscriptions.value.map {

                if (it.id == id) {
                    it.copy(
                        enabled = enabled
                    )
                } else {
                    it
                }
            }
    }

    fun updateInfo(
        id: String,
        profilesCount: Int
    ) {

        _subscriptions.value =
            _subscriptions.value.map {

                if (it.id == id) {

                    it.copy(
                        profilesCount =
                            profilesCount,
                        lastUpdate =
                            System.currentTimeMillis()
                    )

                } else {
                    it
                }
            }
    }

    fun get(
        id: String
    ): Subscription? {

        return _subscriptions
            .value
            .find {
                it.id == id
            }
    }

    fun clear() {

        _subscriptions.value =
            emptyList()
    }
}
