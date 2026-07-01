package com.v2ray.myvpn.viewmodel

import androidx.lifecycle.ViewModel
import com.v2ray.myvpn.model.Subscription
import com.v2ray.myvpn.repository.SubscriptionRepository
import kotlinx.coroutines.flow.StateFlow

class SubscriptionViewModel : ViewModel() {

    val subscriptions:
        StateFlow<List<Subscription>>
        get() =
            SubscriptionRepository
                .subscriptions

    fun addSubscription(
        name: String,
        url: String
    ) {

        if (
            name.isBlank() ||
            url.isBlank()
        ) {
            return
        }

        SubscriptionRepository.add(
            name = name,
            url = url
        )
    }

    fun removeSubscription(
        id: String
    ) {

        SubscriptionRepository.remove(
            id
        )
    }

    fun enableSubscription(
        id: String,
        enabled: Boolean
    ) {

        SubscriptionRepository.enable(
            id,
            enabled
        )
    }

    fun updateSubscription(
        subscription: Subscription
    ) {

        SubscriptionRepository.update(
            subscription
        )
    }

    fun refreshSubscription(
        id: String,
        profilesCount: Int
    ) {

        SubscriptionRepository.updateInfo(
            id,
            profilesCount
        )
    }

    fun getSubscription(
        id: String
    ): Subscription? {

        return SubscriptionRepository
            .get(id)
    }

    fun clear() {

        SubscriptionRepository.clear()
    }
}
