package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.ISubscriptionListener

class  SubscriptionViewModel:BaseViewModel(),ISubscriptionListener{


    val subscriptions
           get() = repository.subscriptions.all

    val selectedPodcastId= MutableLiveData<String>()



    override fun subscribe(subscription: Subscription)= async {
        repository.subscribe(subscription)
    }

    override fun unsubscribe(sub:Subscription) =async {
        repository.unsubscribe("${sub.podcastId}")
    }


    override fun openPodcast(id: String) {
        selectedPodcastId.postValue(id)
    }

}