package com.example.marlonmoorer.streamkast.api.models.rss

import android.graphics.Bitmap
import java.util.*

class Channel{
    var title=""
    var link=""
    var description=""
    var author=""
    var thumbnail=""
    var categories:MutableList<String> = mutableListOf()
    var episodes:MutableList<Episode> = mutableListOf()
    val count
        get() = episodes.size.toString()

}

class  Episode{
    var title=""
    var guid=""
    var publishedDate:Date?=null
    var thumbnail=""
    var duration=""
    var description=""
    var mediaUrl=""
    var length=""
    var imageBitmap: Bitmap?=null
    var author=""
}

