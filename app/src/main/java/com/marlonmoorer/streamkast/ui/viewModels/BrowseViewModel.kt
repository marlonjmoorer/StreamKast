package com.marlonmoorer.streamkast.ui.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

import com.marlonmoorer.streamkast.api.models.Podcast

import com.marlonmoorer.streamkast.data.Featured
import org.jetbrains.anko.doAsync


class  BrowseViewModel:BaseViewModel() {

    lateinit var featuredPodcast:LiveData<List<Featured>>

    lateinit var latestPodcast:LiveData<List<Podcast>>

    fun  setGenre(id: String){

        featuredPodcast= repository.getFeaturedPostcasts(id)
        latestPodcast=Transformations.map(repository.getShowsByGenre(id),{podcasts->
            podcasts.forEach {p->
                doAsync {
                   repository.isSubscribed(p.id).observeForever {
                       p.subscribed=it!!
                       p.notifyChange()
                   }
                }
            }
            return@map podcasts
        })
    }

}
