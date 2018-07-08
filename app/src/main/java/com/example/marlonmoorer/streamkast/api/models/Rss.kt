package com.example.marlonmoorer.streamkast.api.models.rss

import android.graphics.Bitmap
import java.util.*

class Channel{

    var title=""
    var link=""
    var lastUpdateDate:Date?=null
    var description=""
    var author=""
    var thumbnail=""
    var category:List<String> = emptyList()
    var episodes:List<Episode> = emptyList()

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


}

