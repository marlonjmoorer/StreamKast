package com.example.marlonmoorer.streamkast.api.models.chart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.example.marlonmoorer.streamkast.load


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
    var entries:List<PodcastEntry>?=null

}

class Field{
    var label:String?=null
    var attributes:Map<String,String>?=null
}

class PodcastEntry{
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

    val Image
        get() = _images?.map {it.label}!!.last()

    val Summary
        get() = _summary?.label

    val Id
        get() = _id?.attributes!!["im:id"]

    val Artist
        get() = _artist?.label

    val LastUpdateDate
        get() = _date?.label



}


