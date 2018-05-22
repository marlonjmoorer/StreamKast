package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.adapters.SectionModel

import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.FeedResult
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.async


class  BrowseViewModel: ViewModel() {

    private var podcastList = MutableLiveData<List<MediaItem>>()
    private val loading=MutableLiveData<Boolean>()
    private var DEFAULT_COUNT=10;
    private var itunesRepository:ItunesRepository
    private var sections= mutableMapOf<String,MutableLiveData<List<PodcastEntry>?>>()
    var selectedPodcast= MutableLiveData<MediaItem>()

    companion object {
        var FEATURED="Featured"
    }

    val podcasts
        get() = podcastList

    val isLoading
        get()=loading
    init {
        itunesRepository= ItunesRepository()
    }




    fun getSection(key:String,count:Int=DEFAULT_COUNT): MutableLiveData<List<PodcastEntry>?>? {
        var section=sections[key]
        if(section==null){
            section=MutableLiveData()
            sections[key]=section
            async {
                sections[key]?.postValue(this.loadPodcast(key,count))
            }
        }
        return sections[key]
    }

   private fun loadPodcast(key: String,limit:Int=DEFAULT_COUNT):List<PodcastEntry>{
        var genre=MediaGenre.parse(key)
        return genre?.let {
            return  itunesRepository.topPodCast(limit,it)!!
        }?:when(key){
            FEATURED -> itunesRepository.topPodCast(limit)!!
            else-> emptyList()
        }
    }



    fun loadMore(key:String)=async{
        loading.postValue(true)
       // this.podcastList?.postValue(this.loadPodcast(key,50))
        loading.postValue(false)
    }

    fun selectPodcast(podcast:MediaItem){
        this.selectedPodcast.postValue(podcast)
    }


}
