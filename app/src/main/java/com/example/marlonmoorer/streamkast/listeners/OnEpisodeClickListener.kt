package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.Episode

interface OnEpisodeClickListener {
    fun onClick(episode: Episode)

    fun onPlay(episode: Episode){

    }
}