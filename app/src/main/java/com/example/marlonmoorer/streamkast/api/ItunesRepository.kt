package com.example.marlonmoorer.streamkast.api



import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


import com.example.marlonmoorer.streamkast.api.models.*
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.async


import com.google.gson.Gson
import org.json.XML

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.io.File
import java.io.StringReader
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory


//import org.simpleframework.xml.core.Persister




/**
 * Created by marlonmoorer on 3/21/18.
 */
class ItunesRepository {
    val service:ItunesService


    init {
        service= Retrofit.Builder()
                .baseUrl(ItunesService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesService::class.java)

    }


    fun search(query:Map<String, String>): List<MediaItem>? {
        var response=this.service.search(query).execute().body()
        response?.let {
          return  it.results
        }
        return emptyList()
    }
    fun lookup(query:Map<String, String>): List<MediaItem>? {
        var response=this.service.lookup(query).execute().body()
        response?.let {
            return  it.results
        }
        return emptyList()
    }
    fun getPodcastById(id:String):MediaItem?{
        return lookup(mapOf("id" to id))?.firstOrNull()
    }

    fun parseFeed(url:String):Channel?{
        try {
            var xml= URL(url).readText()
            val json = XML.toJSONObject(xml).toString(4)
            var feed= Gson().fromJson(json,RssFeed::class.java)
            return feed.rss!!.channel
        }
       catch (ex:Exception){
           ex.printStackTrace()
           Log.e("help",ex.message)
       }
        return null
    }

    fun topPodCast(limit:Int=10,genre: MediaGenre?=null): List<PodcastEntry>?{

        genre?.let {
            var ls=service.topPodcastByGenre(it.id,limit).execute().body()?.rss?.entries
            return ls
        }
        return service.topPodcast(limit).execute().body()?.rss?.entries
    }

    fun getShowsByGenre(genre: MediaGenre,limit: Int=10): List<MediaItem>? {
        var query=mapOf(
                "term" to "podcast",
                //"limit" to limit.toString(),
                "genreId" to genre.id,
                "entity" to "podcast"
        )
        return search(query)?.sortedByDescending {
            it.releaseDate
        }?.take(limit)

    }

}