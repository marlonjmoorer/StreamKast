package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.ISelectHandler
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.*

import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.api.models.MediaItem


/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel :ViewModel(),ISelectHandler {

    private val channel=MutableLiveData<Channel>()
    private val episodes=MutableLiveData<List<Episode>>()
    private  val selectedEpisode=MutableLiveData<Episode>()
    val queuedEpisode=MutableLiveData<Episode>()

    var repository: Repository
    init {
        repository= Repository()
    }

    fun getPodcast():LiveData<Channel> = channel
    fun getCurrentEpisode():LiveData<Episode> = selectedEpisode
    fun getEpisodes():LiveData<List<Episode>> = episodes

    fun setEpisode(episode: Episode)=selectedEpisode.postValue(episode)
    fun loadPodcast(id:String): LiveData<Channel> {
        async {
            val result= repository.getPodcastById(id)
            val feed= repository.parseFeed(result?.feedUrl!!)
            this.channel.postValue(feed?.channel)
            this.episodes.postValue(feed?.episodes)
        }
        return channel
    }


    override fun onEpisodeSelect(episode:Episode)=selectedEpisode.postValue(episode)

    override fun queueEpisode(episode: Episode) = queuedEpisode.postValue(episode)






}