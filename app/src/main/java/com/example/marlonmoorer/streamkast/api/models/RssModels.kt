package com.example.marlonmoorer.streamkast.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by marlonmoorer on 3/25/18.
 */

class RssFeed {
    @SerializedName("rss")
    @Expose
    var rss: Rss? = null
}

class Rss {

    @SerializedName("channel")
    @Expose
    var channel: Channel? = null

//    @field:Attribute
//    var version: String? = null

}

class Channel {

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("item")
    @Expose
    var item: List<Episode>? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("itunes:author")
    @Expose
    var author: String? = null
    //@Element
   // var language: String? = null

    //var copyright: String? = null
    @SerializedName("image")
    @Expose
    var image: Image? = null



}

class Episode {


    @SerializedName("pubDate")
    @Expose
    var pubDate: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("enclosure")
    @Expose
    var enclosure: Enclosure? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

}

class Enclosure {

    @SerializedName("url")
    @Expose
    var url:String?=null

}
class Image{
    @SerializedName("url")
    @Expose
    var url:String?=null

}

