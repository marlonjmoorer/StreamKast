package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.BR
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.*

import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.Subscription


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

    init {
        async {
            subscribed.value=isSubscribed()
        }
    }

    fun getPodcast():LiveData<Channel> = podcast
    fun getCurrentEpisode():LiveData<Episode> = selectedEpisode
    fun getEpisodes():LiveData<List<Episode>> = episodes
    fun subscribed():LiveData<Boolean> = subscribed
    fun setEpisode(episode: Episode)=selectedEpisode.postValue(episode)

    fun loadPodcast(id:String): LiveData<Channel> {
        async {
            podcastId=id
            val result= repository.getPodcastById(id)
            subscribed.postValue(isSubscribed())
            repository.parseFeed(result?.feedUrl!!)?.run {
                podcast.postValue(channel)
                channel?.count=episodes!!.size.toString()
                episodes?.forEach {
                    if(it.thumbnail.isNullOrEmpty()){
                        it.thumbnail=channel?.image
                    }
                }
                this@DetailViewModel.episodes.postValue(episodes)
            }
        }
        return  podcast
    }
    val subbed
        get() = subscribed.value
    val title
        get() = podcast.value?.title
    private fun isSubscribed()=repository.isSubscribed(podcastId)

    fun toggleSubscription()=async{
        if(repository.isSubscribed(podcastId)){
            repository.unsubscribe(podcastId)
        }else{
            podcast.value?.let{
                repository.subscribe(Subscription().apply {
                    title=it.title
                    thumbnail=it.image
                    podcastId=this@DetailViewModel.podcastId.toInt()
                })
            }
        }
        subscribed.postValue(isSubscribed())
    }









}