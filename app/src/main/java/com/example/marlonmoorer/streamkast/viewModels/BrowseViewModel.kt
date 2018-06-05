package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import com.example.marlonmoorer.streamkast.ISelectHandler

import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.async


class  BrowseViewModel: ViewModel(),ISelectHandler{

    private var podcastList = MutableLiveData<List<MediaItem>>()
    private val loading=MutableLiveData<Boolean>()
    private var DEFAULT_COUNT=10;
    private var itunesRepository:ItunesRepository
    private var sections= mutableMapOf<String,MutableLiveData<List<PodcastEntry>?>>()
    var selectedPodcastId= MutableLiveData<String>()
    var selectedGenre= MutableLiveData<MediaGenre>()
    companion object {
        var FEATURED="Featured"
    }


    val isLoading
        get()=loading

    init {
        itunesRepository= ItunesRepository()

    }

    fun selectGenre(genre: MediaGenre)=this.selectedGenre.postValue(genre)

    override fun onPodcastSelect(id: String) =selectPodcast(id)
    override fun onGenreSelect(genre: MediaGenre)=selectGenre(genre)



    fun getFeaturedByGenre(key:String, count:Int=DEFAULT_COUNT): MutableLiveData<List<PodcastEntry>?>? {
        var section=sections[key]
        if(section==null){
            section=MutableLiveData()
            sections[key]=section
            async {
                sections[key]?.postValue(this.loadFeaturedPodcasts(key,count))
            }
        }
        return sections[key]
    }

    private fun loadFeaturedPodcasts(key: String, limit:Int=DEFAULT_COUNT):List<PodcastEntry>{
        var genre=MediaGenre.parse(key)
        return genre?.let {
            return  itunesRepository.topPodCast(limit,it)!!
        }?:when(key){
            FEATURED -> itunesRepository.topPodCast(limit)!!
            else-> emptyList()
        }
    }


    fun getPodcastByGenre(genre: MediaGenre):MutableLiveData<List<MediaItem>>{
        async {
           this.podcastList.postValue(itunesRepository.getShowsByGenre(genre))
        }
        return  this.podcastList
    }

    fun selectPodcast(podcastId:String)=this.selectedPodcastId.postValue(podcastId)


}
