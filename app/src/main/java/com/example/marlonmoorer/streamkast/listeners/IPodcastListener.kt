package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.Podcast

interface IPodcastListener {

    fun viewPodcast(podcastId:String)

    fun toggleSubscription(podcast:Podcast)
}