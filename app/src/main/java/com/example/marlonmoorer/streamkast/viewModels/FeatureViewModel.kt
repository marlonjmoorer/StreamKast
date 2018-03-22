package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.FeedResult
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem

/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeatureViewModel : ViewModel() {
    var topPodcastList: MutableLiveData<List<MediaItem>>?=null
    var showByGenre =mutableMapOf<MediaGenre,MutableLiveData<List<MediaItem>?>>()
    var limit=5
    var itunesRepository:ItunesRepository
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

    fun loadTopPodcast() = Thread({
            val podcast= itunesRepository.topPodCast(limit)
            topPodcastList?.postValue(podcast)
    }).start()

    fun loadShowByGenre(genre: MediaGenre)=Thread({
        val podcast= itunesRepository.getShowsByGenre(genre,limit)
        showByGenre[genre]?.postValue(podcast)
    }).start()

}
