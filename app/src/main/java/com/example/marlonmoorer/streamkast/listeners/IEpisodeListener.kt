package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.rss.Episode

interface IEpisodeListener {
    fun viewEpisode(episode: Episode)

    fun playEpisode(episode: Episode)
}