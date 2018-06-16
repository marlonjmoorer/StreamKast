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
import com.example.marlonmoorer.streamkast.data.Featured
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.OnSubscribeListener


class  BrowseViewModel:BaseViewModel(),OnSubscribeListener {

    private var podcastList = MutableLiveData<List<MediaItem>>()
    private var DEFAULT_COUNT=10;
    private var selectedPodcastId= MutableLiveData<String>()
    private var selectedGenre= MutableLiveData<MediaGenre>()

    companion object {
        var FEATURED="Featured"
    }

    override fun toggleSubscription(podcast: MediaItem) = async {
        if(repository.isSubscribed(podcast.collectionId)){
            repository.unsubscribe(podcast.collectionId)
        }else{
            repository.subscribe(Subscription().apply {
                title=podcast.collectionName
                thumbnail=podcast.artworkUrl100
                podcastId=podcast.collectionId.toInt()
            })
        }
        podcast.subscribed=repository.isSubscribed(podcast.collectionId)
        podcast.notifyPropertyChanged(BR.subscribed)
    }




    fun setGenre(genre: MediaGenre)=this.selectedGenre.postValue(genre)

    fun getSelectedPodCastId():LiveData<String>{
        return selectedPodcastId
    }

    fun getCurrentGenre():LiveData<MediaGenre> = selectedGenre

    fun getFeaturedByGenre(key:String, count:Int=DEFAULT_COUNT): LiveData<List<Featured>> {
        val genre=MediaGenre.parse(key)!!
        repository.syncFeatured(genre.id)
        return  repository.getFeaturedPostcasts(genre.id)
    }




    fun getPodcastByGenre(genre: MediaGenre):LiveData<List<MediaItem>>{
        async {
            val results=repository.getShowsByGenre(genre)
            results?.forEach {
                it.listener=this
               it.subscribed= repository.isSubscribed(it.collectionId)
            }
            this.podcastList.postValue(results)
        }
        return  this.podcastList
    }

    fun setPodcast(podcastId:String)=this.selectedPodcastId.postValue(podcastId)


}
