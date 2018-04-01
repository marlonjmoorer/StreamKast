package com.example.marlonmoorer.streamkast.adapters

import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem


/**
 * Created by marlonmoorer on 3/22/18.
 */

data class SectionModel(
        val items:List<MediaItem>,
        val genre: MediaGenre?=null,
        val title:String=""){

    val header
        get()=genre?.displayname()?:title
    val key
        get()= genre?.id ?:title

}




