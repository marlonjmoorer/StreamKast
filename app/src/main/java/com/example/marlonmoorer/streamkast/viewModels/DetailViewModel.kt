package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.BR
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.rss.*


import com.example.marlonmoorer.streamkast.data.Subscription
import org.jetbrains.anko.doAsync


/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel :BaseViewModel() {

    private val podcast=MutableLiveData<Channel>()
    private val episodes=MutableLiveData<List<Episode>>()
    private  val selectedEpisode=MutableLiveData<Episode>()
    private val subscribed= MutableLiveData<Boolean>()
    private var podcastId=""
    val queuedEpisode=MutableLiveData<Episode>()


    fun getPodcast():LiveData<Channel> = podcast
    fun getCurrentEpisode():LiveData<Episode> = selectedEpisode
    fun getEpisodes():LiveData<List<Episode>> = episodes
    fun subscribed():LiveData<Boolean> = subscribed
    fun setEpisode(episode: Episode)=selectedEpisode.postValue(episode)

    fun loadPodcast(id:String): LiveData<Channel> {
        podcast.value=null
        doAsync {
            podcastId=id
            val result= repository.getPodcastById(id)
            subscribed.postValue(isSubscribed())
            repository.parseFeed(result?.feedUrl!!)?.let{feed->
                podcast.postValue(feed)
                episodes.postValue(feed.episodes)
            }
        }
        return  podcast
    }
    val subbed
        get() = subscribed.value
    val title
        get() = podcast.value?.title

    private fun isSubscribed()=repository.isSubscribed(podcastId)

    fun toggleSubscription(){
        doAsync {
            if(repository.isSubscribed(podcastId)){
                repository.unsubscribe(podcastId)
            }else{
                podcast.value?.let{
                    repository.subscribe(Subscription().also{sub->
                        sub.title=it.title
                        sub.thumbnail=it.thumbnail
                        sub.podcastId=podcastId.toInt()
                    })
                }
            }
            subscribed.postValue(isSubscribed())
        }
    }

    fun clear(){
        podcast.postValue(null)
        episodes.postValue(null)
        subscribed.postValue(false)
    }









}