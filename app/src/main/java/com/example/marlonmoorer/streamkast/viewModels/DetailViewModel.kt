package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.ISelectHandler
import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.*
import com.example.marlonmoorer.streamkast.async

/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel :ViewModel() {
    private var podcast= MutableLiveData<MediaItem>()
    val channel=MutableLiveData<Channel>()
    private val loading=MutableLiveData<Boolean>()
    val  episodes=MutableLiveData<List<Episode>>()
    val  selectedEpisode=MutableLiveData<Episode>()
    var itunesRepository: ItunesRepository
    init {
        itunesRepository= ItunesRepository()
    }

    val selectedPodcast
        get() = podcast

    fun loadPodcast(id:String): MutableLiveData<MediaItem> {
        async {
            val result= itunesRepository.getPodcastById(id)
            this.podcast.postValue(result)
            val channel= itunesRepository.parseFeed(result?.feedUrl!!)
            this.channel.postValue(channel)
        }
        return podcast
    }

    val handler= object  :ISelectHandler{
        override fun onEpisodeSelect(episode: Episode) {
           this@DetailViewModel.selectedEpisode.postValue(episode)
        }
    }




}