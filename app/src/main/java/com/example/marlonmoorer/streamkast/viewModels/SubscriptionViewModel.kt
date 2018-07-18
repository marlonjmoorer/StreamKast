package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.ISubscriptionListener
import org.jetbrains.anko.doAsync

class  SubscriptionViewModel:BaseViewModel(){


    val subscriptions
           get() = repository.subscriptions.all


    fun toggleSubscription(id:String):LiveData<Boolean>{
        val subscribed=MutableLiveData<Boolean>()
        doAsync {
//            if(repository.isSubscribed(id)){
//                repository.unsubscribe(id)
//                subscribed.postValue(false)
//            }else{
//                repository.getPodcastById(id)?.let{podcast->
//                    repository.subscribe(Subscription().apply {
//                      //  title=podcast.collectionName
//                       // thumbnail=podcast.artworkUrl600
//                       // podcastId=podcast.collectionId.toInt()
//                    })
//                    subscribed.postValue(true)
//                }
//
//            }
        }
        return repository.isSubscribed(id)
    }

    fun subscribe(sub:Subscription){
        doAsync {
            repository.subscribe(sub)
        }

    }

    fun unsubscribe(sub:Subscription) {
        doAsync {
            repository.unsubscribe("${sub.podcastId}")
        }
    }


}