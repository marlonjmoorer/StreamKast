package com.example.marlonmoorer.streamkast.api.models.chart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by marlonmoorer on 4/1/18.
 */
class ChartResult {

    @SerializedName("feed")
    @Expose
    var rss:Feed? = null
}

class Feed{

    @SerializedName("entry")
    @Expose
    var entries:List<Entry>?=null

}

class Field{
    var label:String?=null
    var attributes:Map<String,String>?=null
}

class Entry{
    @SerializedName("im:name")
    @Expose
    var _name:Field?=null

    @SerializedName("im:image")
    @Expose
    var _images: List<Field>?=null

    @SerializedName("summary")
    @Expose
    var _summary:Field?=null

    @SerializedName("id")
    @Expose
    var _id:Field?=null

    @SerializedName("im:artist")
    @Expose
    var _artist:Field?=null

    @SerializedName("releaseDate")
    @Expose
    var _date:Field?=null


    val Name
        get() = _name?.label

    val Images
        get() = _images?.map { it.label }

    val Summary
        get() = _summary?.label

    val Id
        get() = _id?.label

    val Artist
        get() = _artist?.label

    val LastUpdateDate
        get() = _date?.label
}


