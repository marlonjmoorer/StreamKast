package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.marlonmoorer.streamkast.BR
import com.example.marlonmoorer.streamkast.api.PagedCategoryDataSource
import com.example.marlonmoorer.streamkast.api.PagedSearchDataSource

import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.data.Featured
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.IGenreListener
import com.example.marlonmoorer.streamkast.listeners.IPodcastListener
import org.jetbrains.anko.doAsync
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class  BrowseViewModel:BaseViewModel() {

    lateinit var featuredPodcast:LiveData<List<Featured>>

    lateinit var latestPodcast:LiveData<List<Podcast>>

    fun  setGenre(id: String){

        featuredPodcast= repository.getFeaturedPostcasts(id)
        latestPodcast=repository.getShowsByGenre(id)
    }

}
