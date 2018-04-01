package com.example.marlonmoorer.streamkast.api

import com.example.marlonmoorer.streamkast.api.models.ItunesRssFeed
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import com.example.marlonmoorer.streamkast.api.models.chart.ChartResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import  retrofit2.http.FieldMap
import retrofit2.http.QueryMap


/**
 * Created by marlonmoorer on 3/21/18.
 */
interface ItunesService {


    companion object {
        val baseUrl: String
            get() = "https://itunes.apple.com/"
        val LIMIT=25
    }

    @GET("us/rss/topaudiopodcasts/limit={limit}/json")
    fun topPodcast(@Path("limit") limit: Int=LIMIT): Call<ChartResult>

    @GET("us/rss/topaudiopodcasts/limit={limit}/genre={genreId}/json")
    fun topPodcastByGenre(@Path("genreId") id:String, @Path("limit") limit: Int=LIMIT ): Call<ChartResult>

    @GET("search")
    fun search(@QueryMap fields:Map<String, String> ): Call<SearchResults>

    @GET("lookup")
    fun lookup(@QueryMap fields:Map<String, String> ): Call<SearchResults>

}

interface ItunesRssService {

    companion object {
        val baseUrl: String
            get() = "https://rss.itunes.apple.com/api/v1/us/podcasts/"
    }
    @GET("top-podcasts/all/{limit}/explicit.json")
    fun topPodcast(@Path("limit") limit:Int): Call<ItunesRssFeed>



}