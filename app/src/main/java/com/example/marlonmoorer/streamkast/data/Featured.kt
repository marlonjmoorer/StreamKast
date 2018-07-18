package com.example.marlonmoorer.streamkast.data

import android.arch.persistence.room.*

@Entity(primaryKeys = arrayOf("podcastId","genreId"))
class Featured{
    var podcastId=""
    var name=""
    var imageUrl=""
    var summary=""
    var author=""
   // var lastUpdateDate= Date()
    var genreId=""
}

