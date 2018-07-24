package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData

import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.data.Featured


class  BrowseViewModel:BaseViewModel() {

    lateinit var featuredPodcast:LiveData<List<Featured>>

    lateinit var latestPodcast:LiveData<List<Podcast>>

    fun  setGenre(id: String){

        featuredPodcast= repository.getFeaturedPostcasts(id)
        latestPodcast=repository.getShowsByGenre(id)
    }

}
