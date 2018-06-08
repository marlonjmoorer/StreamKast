package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.ISelectHandler
import com.example.marlonmoorer.streamkast.api.ItunesRepository

import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.github.magneticflux.rss.namespaces.standard.elements.Channel
import com.github.magneticflux.rss.namespaces.standard.elements.Item

/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel :ViewModel(),ISelectHandler {
    private var podcast= MutableLiveData<MediaItem>()
    val channel=MutableLiveData<Channel>()
    val episodes=MutableLiveData<List<Item>>()
    val selectedEpisode=MutableLiveData<Item>()
    val queuedEpisode=MutableLiveData<Item>()

    var itunesRepository: ItunesRepository
    init {
        itunesRepository= ItunesRepository()
    }

    val selectedPodcast
        get() = podcast
    val currentEpisode
            get()=selectedEpisode.value


    fun loadPodcast(id:String): MutableLiveData<Channel> {
        async {
            val result= itunesRepository.getPodcastById(id)
            val channel= itunesRepository.parseFeed(result?.feedUrl!!)
            this.channel.postValue(channel)
        }
        return channel
    }


    override fun onEpisodeSelect(episode:Item)=selectedEpisode.postValue(episode)

    override fun queueEpisode(episode: Item) = queuedEpisode.postValue(episode)






}