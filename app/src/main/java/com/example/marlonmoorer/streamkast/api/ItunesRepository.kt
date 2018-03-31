package com.example.marlonmoorer.streamkast.api



import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


import com.example.marlonmoorer.streamkast.api.models.*
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
    val rssService:ItunesRssService
    var topPodcastIds= emptyList<String?>()

    init {
        val builder= Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())

        service= builder
                .baseUrl(ItunesService.baseUrl)
                .build()
                .create(ItunesService::class.java)

        rssService=builder
                .baseUrl(ItunesRssService.baseUrl)
                .build()
                .create(ItunesRssService::class.java)

            async {
                topPodcastIds= this.rssService.topPodcast(200).execute().body()?.feed?.results?.map {
                    it.id
                }!!
            }




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


    fun ParseFeed(url:String):Channel?{
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




    fun topPodCast(limit:Int=10): List<MediaItem>? {
        topPodcastIds= this.rssService.topPodcast(200).execute().body()?.feed?.results?.map {
            it.id
        }!!
        val items= topPodcastIds.take(limit)!!
        var query=mapOf(
                "id" to items.joinToString(",")
                //"limit" to limit.toString(),
        )
        return lookup(query)
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