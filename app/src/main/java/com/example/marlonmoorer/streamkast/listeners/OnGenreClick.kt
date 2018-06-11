package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.MediaGenre

interface OnGenreClick {

    fun onClick(genre: MediaGenre)
}