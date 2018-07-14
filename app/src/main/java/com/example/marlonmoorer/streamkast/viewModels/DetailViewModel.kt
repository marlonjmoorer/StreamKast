package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.BR
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.rss.*


import com.example.marlonmoorer.streamkast.data.Subscription
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.doAsync


/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel :BaseViewModel() {

    var podcastDetails:LiveData<Channel>
    private val podcast:MutableLiveData<Podcast>
    val episodes:LiveData<List<Episode>>
    private  val selectedEpisode=MutableLiveData<Episode>()
    val subscribed:LiveData<Boolean>
    private var podcastId=""
    val queuedEpisode=MutableLiveData<Episode>()
    init {
        podcast=MutableLiveData()
        podcastDetails=Transformations.switchMap(podcast,{p->
            return@switchMap repository.parseFeed(p.feedUrl!!)
        })
        episodes=Transformations.map(podcastDetails,{channel-> channel.episodes })
        subscribed=Transformations.map(podcast,{p->p.subscribed})
    }



    fun getCurrentEpisode():LiveData<Episode> = selectedEpisode
    fun setEpisode(episode: Episode)=selectedEpisode.postValue(episode)

    fun loadPodcast(id:String): LiveData<Podcast> {
        doAsync {
            podcastId=id
            val result= repository.getPodcastById(id)
            result?.subscribed=repository.isSubscribed(id)
            podcast.postValue(result)
        }
        return  podcast
    }
    val isSubbed
        get() = subscribed.value==true
    val title
        get() = podcastDetails.value?.title


    fun toggleSubscription(){
        doAsync {
            if(repository.isSubscribed(podcastId)){
                repository.unsubscribe(podcastId)
            }else{
                podcastDetails.value?.let{
                    repository.subscribe(Subscription().also{sub->
                        sub.title=it.title
                        sub.thumbnail=it.thumbnail
                        sub.podcastId=podcastId.toInt()
                    })
                }
            }
        }
    }











}