package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.data.Subscription


interface  ISubscriptionListener{
    fun unsubscribe(sub:Subscription)

    fun openPodcast(id:String)

    fun subscribe(sub: Subscription)
}