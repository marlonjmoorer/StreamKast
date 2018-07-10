package com.example.marlonmoorer.streamkast.api

import android.content.res.Resources
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.chart.ChartResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RssToJsonService{

    companion object {
        val baseUrl: String = "https://api.rss2json.com/v1/"
        private var rssApiKey= ""
        fun setApiKey(key: String)
        {
            rssApiKey=key
        }
    }

//    @GET("api.json")
//    fun parseFeed(@Query("rss_url")rssUrl:String,@Query("api_key")key:String= rssApiKey,@Query("count")count:Int=50): Call<RssResult>
}