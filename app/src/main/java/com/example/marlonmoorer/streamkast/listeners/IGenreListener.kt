package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.MediaGenre

interface IGenreListener {

    fun selectGenre(genre: MediaGenre)
}