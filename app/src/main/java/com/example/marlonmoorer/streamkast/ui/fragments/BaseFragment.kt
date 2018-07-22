package com.example.marlonmoorer.streamkast.ui.fragments

import android.support.v4.app.Fragment

import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.listeners.IGenreListener
import com.example.marlonmoorer.streamkast.listeners.IPodcastListener
import com.example.marlonmoorer.streamkast.listeners.ISubscriptionListener


abstract class   BaseFragment: Fragment(){


    val podcastListener
        get() =  activity as IPodcastListener

    val genreListener
        get() =  activity as IGenreListener

    val episodeListener
        get() =  activity as IEpisodeListener
    val subscriptionListener
        get() =  activity as ISubscriptionListener



}