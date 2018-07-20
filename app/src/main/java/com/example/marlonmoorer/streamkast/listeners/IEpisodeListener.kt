package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.Episode
import com.example.marlonmoorer.streamkast.models.IEpisode

interface IEpisodeListener {
    fun viewEpisode(episode: IEpisode)

    fun playEpisode(episode: IEpisode)
}