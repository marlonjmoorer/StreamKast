package com.example.marlonmoorer.streamkast.adapters

import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem


/**
 * Created by marlonmoorer on 3/22/18.
 */

interface SectionListener{
    fun onSelectItem(item: MediaItem)
    fun onShowMore(genre: MediaGenre?)
}
data class SectionModel(
        val items:List<MediaItem>,
        val genre: MediaGenre?=null,
        val title:String="",
        val listener:SectionListener?=null){

    val header
        get()=genre?.displayname()?:title

    fun showMore()=listener?. onShowMore(genre)
    fun selectItem(item: MediaItem)=listener?.onSelectItem(item)

}




