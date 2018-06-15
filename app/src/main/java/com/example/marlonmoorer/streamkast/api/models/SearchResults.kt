package com.example.marlonmoorer.streamkast.api.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.marlonmoorer.streamkast.listeners.OnSubscribeListener
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SearchResults{

    @SerializedName("resultCount")
    @Expose
    var resultCount: Long? = null
    @SerializedName("results")

    @Expose
    var results: List<MediaItem>? = null

}

class MediaItem:BaseObservable(){



    @SerializedName("collectionId")
    @Expose
    var collectionId: String = ""

    @SerializedName("artistId")
    @Expose
    var artistId: String? = null
    @SerializedName("trackId")
    @Expose
    var trackId: Long? = null
    @SerializedName("artistName")
    @Expose
    var artistName: String? = null
    @SerializedName("collectionName")
    @Expose
    var collectionName: String? = null
    @SerializedName("trackName")
    @Expose
    var trackName: String? = null



    @SerializedName("feedUrl")
    @Expose
    var feedUrl: String? = null

    @SerializedName("artworkUrl30")
    @Expose
    var artworkUrl30: String? = null
    @SerializedName("artworkUrl60")
    @Expose
    var artworkUrl60: String? = null
    @SerializedName("artworkUrl100")
    @Expose
    var artworkUrl100: String? = null

    @SerializedName("releaseDate")
    @Expose
    var releaseDate: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null


    @SerializedName("artworkUrl600")
    @Expose
    var artworkUrl600: String? = null


    @Bindable
    var subscribed= false

    var listener: OnSubscribeListener?=null

    fun subscribe(){
        listener?.toggleSubscription(this)
    }



}