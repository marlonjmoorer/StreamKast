package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.BR

import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.OnSubscribeListener


class  BrowseViewModel:BaseViewModel(),OnSubscribeListener {

    private var podcastList = MutableLiveData<List<MediaItem>>()
    private var DEFAULT_COUNT=10;
    private var sections= mutableMapOf<String,MutableLiveData<List<PodcastEntry>?>>()
    private var selectedPodcastId= MutableLiveData<String>()
    private var selectedGenre= MutableLiveData<MediaGenre>()

    companion object {
        var FEATURED="Featured"
    }

    override fun toggleSubscription(podcast: MediaItem) {

        async {

            with(database.SubscriptionDao()){
                if(podcast.subscribed){

                    val sub=  getById(podcast.collectionId)
                    delete(sub)
                    podcast.subscribed=false
                }
                else{
                    val sub=Subscription().apply {
                        showId=podcast.collectionId.toInt()
                        title=podcast.collectionName
                        thumbnail=podcast.artworkUrl100
                    }
                    insert(sub)
                }
                podcast.subscribed=exist(podcast.collectionId)
                podcast.notifyPropertyChanged(BR.subscribed)
            }

        }
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
            val results=repository.getShowsByGenre(genre)
            results?.forEach {
                it.listener=this
                it.subscribed= database.SubscriptionDao().exist(it.collectionId)
            }
            this.podcastList.postValue(results)
        }
        return  this.podcastList
    }

    fun setPodcast(podcastId:String)=this.selectedPodcastId.postValue(podcastId)


}
