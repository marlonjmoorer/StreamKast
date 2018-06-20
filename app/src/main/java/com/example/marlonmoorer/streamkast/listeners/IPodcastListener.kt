package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.Podcast

interface IPodcastListener {

    fun open(podcastId:String)

    fun toggleSubscription(podcast:Podcast)
}