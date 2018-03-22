package com.example.marlonmoorer.streamkast.api

import com.example.marlonmoorer.streamkast.api.models.ItunesRssFeed
import com.example.marlonmoorer.streamkast.api.models.SearchResults
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
    }

    @GET("search")
    fun search(@QueryMap fields:Map<String, String> ): Call<SearchResults>

    @GET("lookup")
    fun lookup(@FieldMap fields:Map<String, String> ): Call<List<ResponseBody>>

}

interface ItunesRssService {

    companion object {
        val baseUrl: String
            get() = "https://rss.itunes.apple.com/api/v1/us/podcasts/"
    }
    @GET("top-podcasts/all/{limit}/explicit.json")
    fun topPodcast(@Path("limit") limit:Int): Call<ItunesRssFeed>



}