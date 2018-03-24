package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.FeedResult
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.async


class FeatureViewModel : ViewModel() {
    private var topPodcastList: MutableLiveData<List<MediaItem>>?=null
    private var showByGenre =mutableMapOf<MediaGenre,MutableLiveData<List<MediaItem>?>>()
    private var podcastList = MutableLiveData<List<MediaItem>>()
    private val loading=MutableLiveData<Boolean>()
    private var limit=6
    private var itunesRepository:ItunesRepository


    val podcast
        get() = podcastList
    val isLoading
        get()=loading
    init {
        itunesRepository= ItunesRepository()
    }

    fun getFeatured(): MutableLiveData<List<MediaItem>>? {
        if (topPodcastList==null) {
            topPodcastList= MutableLiveData<List<MediaItem>>();
            loadTopPodcast();
        }
        return topPodcastList;
    }

    fun getShowsByGenre(genre: MediaGenre):MutableLiveData<List<MediaItem>?>{
        if (showByGenre[genre]==null){
            showByGenre[genre]=MutableLiveData<List<MediaItem>?>()
            loadShowByGenre(genre)
        }
        return showByGenre[genre]!!
    }

    fun loadTopPodcast() = async{
            val podcast= itunesRepository.topPodCast(limit)
            topPodcastList?.postValue(podcast)
    }

    fun loadShowByGenre(genre: MediaGenre)=async{
        val podcast= itunesRepository.getShowsByGenre(genre,limit)
        showByGenre[genre]?.postValue(podcast)
    }

    fun loadMore(genre: MediaGenre?)=async{
        loading.postValue(true)
        var podcast=genre?.let {
            itunesRepository.getShowsByGenre(genre,limit = 50)!!
        }?:run{
            itunesRepository.topPodCast(limit = 50)
        }
        loading.postValue(false)
        this.podcastList?.postValue(podcast)
    }


}
