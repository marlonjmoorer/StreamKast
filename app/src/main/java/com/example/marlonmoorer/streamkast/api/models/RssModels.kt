package com.example.marlonmoorer.streamkast.api.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.marlonmoorer.streamkast.listeners.OnSubscribeListener
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName





class Enclosure {

    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("length")
    @Expose
    var length: Int? = null
    @SerializedName("duration")
    @Expose
    var duration: Int? = null


}

class RssResult {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("feed")
    @Expose
    var channel:Channel? = null
    @SerializedName("items")
    @Expose
    var episodes: List<Episode>? = null

}

class Channel {

    @SerializedName("url")
    @Expose
    var url: String? = null


    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("link")
    @Expose
    var link: String? = null


    @SerializedName("author")
    @Expose
    var author: String? = null


    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null


}

class Episode {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("pubDate")
    @Expose
    var pubDate: String? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("guid")
    @Expose
    var guid: String? = null
    @SerializedName("author")
    @Expose
    var author: String? = null
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("enclosure")
    @Expose
    var enclosure: Enclosure? = null
    @SerializedName("categories")
    @Expose
    var categories: List<Any>? = null

}

