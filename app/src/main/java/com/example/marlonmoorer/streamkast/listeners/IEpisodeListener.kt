package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.rss.Episode

interface IEpisodeListener {
    fun open(episode: Episode)

    fun play(episode: Episode){

    }
}