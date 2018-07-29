package com.example.marlonmoorer.streamkast.ui.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.example.marlonmoorer.streamkast.Utils
import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.models.IPodcast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class  SubscriptionViewModel:BaseViewModel(){


    val subscriptions
           get() = repository.subscriptions.all


    fun toggleSubscription(podcast: Podcast):LiveData<Boolean>{
        val isSubbed=MediatorLiveData<Boolean>()
        doAsync {
            if(podcast.subscribed==true){
                repository.unsubscribe(podcast.id)
            }else{
                repository.subscribe(Subscription().apply {
                    title=podcast.title
                    thumbnail=podcast.thumbnail
                    podcastId=podcast.id.toInt()
                })
            }
            isSubbed.postValue(podcast.subscribed)
        }
        return  isSubbed
    }

    fun subscribe(sub:Subscription){
        doAsync(Utils.exceptionHandler){
            repository.subscribe(sub)
        }

    }

    fun unsubscribe(sub:Subscription) {
        doAsync(Utils.exceptionHandler){
            repository.unsubscribe("${sub.podcastId}")
        }
    }


}