package com.example.marlonmoorer.streamkast.ui.viewModels

import android.arch.lifecycle.*
import com.example.marlonmoorer.streamkast.Utils
import com.example.marlonmoorer.streamkast.api.models.Channel
import com.example.marlonmoorer.streamkast.api.models.Episode
import com.example.marlonmoorer.streamkast.api.models.Podcast



import com.example.marlonmoorer.streamkast.data.Subscription
import org.jetbrains.anko.doAsync


/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel:BaseViewModel() {

    lateinit var podcastDetails:LiveData<Channel>
    lateinit  var episodes:LiveData<List<Episode>>
    lateinit  var subscribed:LiveData<Boolean>
    private lateinit var podcast:LiveData<Podcast>
    private var podcastId=""

    fun loadPodcast(id: String){
        podcastId=id
        podcast=repository.getPodcastById(podcastId)
        podcastDetails=Transformations.switchMap(podcast,{p-> repository.parseFeed(p.feedUrl) })
        episodes=Transformations.map(podcastDetails,{channel-> channel.episodes })
        subscribed=Transformations.switchMap(podcast,{p->repository.isSubscribed(p.collectionId)})
    }


    fun getPodcast()=podcast


    val isSubbed
        get() = subscribed.value==true
    val title
        get() = podcastDetails.value?.title


    fun toggleSubscription(){
        doAsync(Utils.exceptionHandler) {
            if(isSubbed){
                repository.unsubscribe(podcastId)
            }else{
                podcastDetails.value?.let{
                    repository.subscribe(Subscription().also{sub->
                        sub.title=it.title
                        sub.thumbnail=it.thumbnail
                        sub.podcastId= podcastId.toInt()
                    })
                }
            }
        }
    }

}