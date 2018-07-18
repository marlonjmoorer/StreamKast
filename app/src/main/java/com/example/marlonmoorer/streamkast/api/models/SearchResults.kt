package com.example.marlonmoorer.streamkast.api.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import com.example.marlonmoorer.streamkast.models.IPodcast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SearchResults{

    @SerializedName("resultCount")
    @Expose
    var resultCount: Long? = null

    @SerializedName("results")
    @Expose
    var results: List<Podcast>? = null

}

class Podcast:BaseObservable(),IPodcast{

    override var id: String=""
        get() = collectionId

    override var title: String=""
        get() =  collectionName

    override var description: String=""

    override var author: String=""
        get() = artistName

    override var thumbnail: String=""
        get() = artworkUrl100

    override var link: String=""
        get() = feedUrl
    @Bindable
    var subscribed= false

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
    var artistName: String = ""
    @SerializedName("collectionName")
    @Expose
    var collectionName: String = ""
    @SerializedName("trackName")
    @Expose
    var trackName: String? = null

    @SerializedName("feedUrl")
    @Expose
    var feedUrl: String=""

    @SerializedName("artworkUrl30")
    @Expose
    var artworkUrl30: String? = null
    @SerializedName("artworkUrl60")
    @Expose
    var artworkUrl60: String? = null
    @SerializedName("artworkUrl100")
    @Expose
    var artworkUrl100: String = ""

    @SerializedName("releaseDate")
    @Expose
    var releaseDate: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null


    @SerializedName("artworkUrl600")
    @Expose
    var artworkUrl600: String? = null





}