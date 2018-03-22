package com.example.marlonmoorer.streamkast.api.models

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

class MediaItem {

    @SerializedName("wrapperType")
    @Expose
    var wrapperType: String? = null
    @SerializedName("kind")
    @Expose
    var kind: String? = null
    @SerializedName("collectionId")
    @Expose
    var collectionId: Long? = null
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
    @SerializedName("collectionCensoredName")
    @Expose
    var collectionCensoredName: String? = null
    @SerializedName("trackCensoredName")
    @Expose
    var trackCensoredName: String? = null
    @SerializedName("collectionViewUrl")
    @Expose
    var collectionViewUrl: String? = null
    @SerializedName("feedUrl")
    @Expose
    var feedUrl: String? = null
    @SerializedName("trackViewUrl")
    @Expose
    var trackViewUrl: String? = null
    @SerializedName("artworkUrl30")
    @Expose
    var artworkUrl30: String? = null
    @SerializedName("artworkUrl60")
    @Expose
    var artworkUrl60: String? = null
    @SerializedName("artworkUrl100")
    @Expose
    var artworkUrl100: String? = null
    @SerializedName("collectionPrice")
    @Expose
    var collectionPrice: Double? = null
    @SerializedName("trackPrice")
    @Expose
    var trackPrice: Double? = null
    @SerializedName("trackRentalPrice")
    @Expose
    var trackRentalPrice: Long? = null
    @SerializedName("collectionHdPrice")
    @Expose
    var collectionHdPrice: Long? = null
    @SerializedName("trackHdPrice")
    @Expose
    var trackHdPrice: Long? = null
    @SerializedName("trackHdRentalPrice")
    @Expose
    var trackHdRentalPrice: Long? = null
    @SerializedName("releaseDate")
    @Expose
    var releaseDate: String? = null
    @SerializedName("collectionExplicitness")
    @Expose
    var collectionExplicitness: String? = null
    @SerializedName("trackExplicitness")
    @Expose
    var trackExplicitness: String? = null
    @SerializedName("trackCount")
    @Expose
    var trackCount: Long? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("primaryGenreName")
    @Expose
    var primaryGenreName: String? = null
    @SerializedName("contentAdvisoryRating")
    @Expose
    var contentAdvisoryRating: String? = null
    @SerializedName("artworkUrl600")
    @Expose
    var artworkUrl600: String? = null
    @SerializedName("genreIds")
    @Expose
    var genreIds: List<String>? = null
    @SerializedName("genres")
    @Expose
    var genres: List<String>? = null

}