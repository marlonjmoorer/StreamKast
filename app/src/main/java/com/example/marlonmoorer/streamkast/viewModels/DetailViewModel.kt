package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
    var itunesRepository: ItunesRepository
    init {
        itunesRepository= ItunesRepository()
    }

    val selectedPodcast
        get() = podcast

    fun loadPodcast(id:String): MutableLiveData<MediaItem> {
        async {
            var result= itunesRepository.getPodcastById(id)
            this.podcast.postValue(result)
            var channel= itunesRepository.parseFeed(result?.feedUrl!!)
            this.channel.postValue(channel)
           /// episodes.postValue(channel?.episodes)
        }
        return podcast
    }





}