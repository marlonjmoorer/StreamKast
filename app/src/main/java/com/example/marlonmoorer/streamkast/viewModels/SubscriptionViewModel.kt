package com.example.marlonmoorer.streamkast.viewModels

import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.Subscription

class  SubscriptionViewModel:BaseViewModel(){


    val subscriptions
           get() = repository.subscriptions.all

    fun unsubscribe(id:Int)= async { repository.unsubscribe("${id}")}

    fun subscribe(subscription: Subscription)= async {
        repository.subscribe(subscription)
    }

}