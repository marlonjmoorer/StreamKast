package com.example.marlonmoorer.streamkast.api



import android.net.Uri
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


import com.example.marlonmoorer.streamkast.api.models.*

import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry








//import org.simpleframework.xml.core.Persister




/**
 * Created by marlonmoorer on 3/21/18.
 */
class Repository {
    val service:ItunesService

    val parseService:RssToJsonService


    init {
        service= Retrofit.Builder()
                .baseUrl(ItunesService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesService::class.java)


        parseService=Retrofit.Builder()
                .baseUrl(RssToJsonService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RssToJsonService::class.java)

    }


    fun search(query:Map<String, String>): SearchResults? {
      return this.service.search(query).execute().body()
    }
    fun lookup(query:Map<String, String>): List<MediaItem>? {
        val response=this.service.lookup(query).execute().body()
        response?.let {
            return  it.results
        }
        return emptyList()
    }
    fun getPodcastById(id:String):MediaItem?{
        return lookup(mapOf("id" to id))?.firstOrNull()
    }

    fun parseFeed(feedUrl:String,page:String="1"): RssResult?{
        try {
             val url = Uri.parse(feedUrl)
                    .buildUpon()
                    .appendQueryParameter("page",page)
                    .build().toString()
            val result=parseService.parseFeed(feedUrl).execute().body()
            return result
        }
       catch (ex:Exception){
           ex.printStackTrace()
           Log.e("help",ex.message)
       }
        return null
    }

    fun topPodCast(limit:Int=10,genre: MediaGenre?=null): List<PodcastEntry>?{

        genre?.let {
            return service.topPodcastByGenre(it.id,limit).execute().body()?.rss?.entries
        }
        return service.topPodcast(limit).execute().body()?.rss?.entries
    }

    fun getShowsByGenre(genre: MediaGenre,limit: Int=10): List<MediaItem>? {
        val query=mapOf(
                "term" to "podcast",
                "genreId" to genre.id,
                "entity" to "podcast"
        )

        return search(query)?.results?.sortedByDescending {
            it.releaseDate
        }?.take(limit)

    }


}


