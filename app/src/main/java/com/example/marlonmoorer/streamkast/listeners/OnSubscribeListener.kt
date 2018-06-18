package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.data.Subscription

interface OnSubscribeListener {
    fun toggleSubscription(podcast:MediaItem)
}
interface  OnSubscriptionMenuListener{
    fun unSubscribe(sub:Subscription)
}