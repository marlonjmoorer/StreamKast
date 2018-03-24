package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.Genre
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.async
import com.github.magneticflux.rss.namespaces.standard.elements.Item
import java.util.*

/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel :ViewModel() {
    private var podcast= MutableLiveData<MediaItem>()
    private val loading=MutableLiveData<Boolean>()
    private  val  episodes=MutableLiveData<List<Item>>()
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


    fun selectShow(podcast:MediaItem)={
        loadEpisodes(podcast.feedUrl!!)
        this.podcast.postValue(podcast)
    }

    fun loadEpisodes(url:String)=async {
        var channel= itunesRepository.ParseFeed(url)
        episodes.postValue(channel.items)
    }




}