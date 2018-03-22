package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.FeedResult
import com.example.marlonmoorer.streamkast.api.models.MediaItem

/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeatureViewModel : ViewModel() {
    var topPodcastList: MutableLiveData<List<FeedResult>>?=null
    var itunesRepository:ItunesRepository
    init {
        itunesRepository= ItunesRepository()
    }

    fun getFeatured(): MutableLiveData<List<FeedResult>>? {
        if (topPodcastList==null) {
            topPodcastList= MutableLiveData<List<FeedResult>>();
            loadTopPodcast();
        }
        return topPodcastList;
    }

    fun loadTopPodcast(){

        val thread = Thread({
            val podcast= itunesRepository.topPodCast()
            topPodcastList?.postValue(podcast)
        }).start()
    }


}
