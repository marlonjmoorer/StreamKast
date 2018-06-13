package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.async


class  BrowseViewModel:BaseViewModel() {

    private var podcastList = MutableLiveData<List<MediaItem>>()
    private var DEFAULT_COUNT=10;
    private var sections= mutableMapOf<String,MutableLiveData<List<PodcastEntry>?>>()
    private var selectedPodcastId= MutableLiveData<String>()
    private var selectedGenre= MutableLiveData<MediaGenre>()

    companion object {
        var FEATURED="Featured"
    }



    fun setGenre(genre: MediaGenre)=this.selectedGenre.postValue(genre)

    fun getSelectedPodCastId():LiveData<String>{
        return selectedPodcastId
    }

    fun getCurrentGenre():LiveData<MediaGenre> = selectedGenre

    fun getFeaturedByGenre(key:String, count:Int=DEFAULT_COUNT): LiveData<List<PodcastEntry>?> {
        var section=sections[key]
        if(section==null){
            section=MutableLiveData()
            sections[key]=section
            async {
                sections[key]?.postValue(this.loadFeaturedPodcasts(key,count))
            }
        }
        return sections[key]!!
    }

    private fun loadFeaturedPodcasts(key: String, limit:Int=DEFAULT_COUNT):List<PodcastEntry>{
        val genre=MediaGenre.parse(key)
        return genre?.let {
            return  repository.topPodCast(limit,it)!!
        }?:when(key){
            FEATURED -> repository.topPodCast(limit)!!
            else-> emptyList()
        }
    }


    fun getPodcastByGenre(genre: MediaGenre):LiveData<List<MediaItem>>{
        async {
           this.podcastList.postValue(repository.getShowsByGenre(genre))
        }
        return  this.podcastList
    }

    fun setPodcast(podcastId:String)=this.selectedPodcastId.postValue(podcastId)


}
