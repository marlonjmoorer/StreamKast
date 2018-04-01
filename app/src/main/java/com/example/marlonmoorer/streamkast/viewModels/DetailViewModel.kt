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
    private var podcast= MutableLiveData<Channel>()
    private val loading=MutableLiveData<Boolean>()
    private  val  episodes=MutableLiveData<List<Episode>>()
    var itunesRepository: ItunesRepository
    init {
        itunesRepository= ItunesRepository()
    }

    val selectedPodcast
        get() = podcast
    val isLoading
        get()=loading
    val getEpisodes
        get() = episodes


    fun selectShow(podcast:MediaItem)=async{
        load(podcast.feedUrl!!)
       // this.podcast.postValue(podcast)
    }

    fun load(url:String)=async {
        var channel=itunesRepository.ParseFeed(url)
        podcast.postValue(channel)
        episodes.postValue(channel?.item)
    }




}