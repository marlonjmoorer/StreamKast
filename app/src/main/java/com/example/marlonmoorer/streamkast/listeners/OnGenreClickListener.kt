package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.MediaGenre

interface OnGenreClickListener {

    fun onClick(genre: MediaGenre)
}