package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.example.marlonmoorer.streamkast.Utils

import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.models.IPodcast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class  SubscriptionViewModel:BaseViewModel(){


    val subscriptions
           get() = repository.subscriptions.all


    fun toggleSubscription(podcast: IPodcast):LiveData<Boolean>{
        val isSubbed=MediatorLiveData<Boolean>()
        val wasSubbed=repository.isSubscribed(podcast.id)
        isSubbed.addSource(wasSubbed,{subbed->
            doAsync {
                if(subbed==true){
                    repository.unsubscribe(podcast.id)
                    isSubbed.postValue(false)
                }else{
                    repository.subscribe(Subscription().apply {
                        title=podcast.title
                        thumbnail=podcast.thumbnail
                        podcastId=podcast.id.toInt()
                    })
                    isSubbed.postValue(true)
                }
                uiThread {
                    isSubbed.removeSource(wasSubbed)
                }
            }
        })
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