package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.marlonmoorer.streamkast.BR

import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.data.Featured
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.IGenreListener
import com.example.marlonmoorer.streamkast.listeners.IPodcastListener
import org.jetbrains.anko.doAsync


class  BrowseViewModel:BaseViewModel() {

    private var podcastList = MutableLiveData<List<Podcast>>()
    private var DEFAULT_COUNT=10;
    private var selectedPodcastId= MutableLiveData<String>()







    fun getSelectedPodCastId():LiveData<String>{
        return selectedPodcastId
    }


    fun getFeaturedByGenre(key:String, count:Int=DEFAULT_COUNT): LiveData<List<Featured>> {
        val genre=MediaGenre.parse(key)!!
        repository.syncFeatured(genre.id)
        return  repository.getFeaturedPostcasts(genre.id)
    }




    fun getPodcastByGenre(genre: MediaGenre):LiveData<List<Podcast>>{
        doAsync {
            val results=repository.getShowsByGenre(genre)
            results?.forEach {

               it.subscribed= repository.isSubscribed(it.collectionId)
            }
            podcastList.postValue(results)
        }
        return  this.podcastList
    }

    fun setPodcast(podcastId:String)=this.selectedPodcastId.postValue(podcastId)


}
