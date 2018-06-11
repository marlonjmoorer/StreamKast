package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.Episode

interface OnEpisodeClick {
    fun onClick(episode: Episode)
}