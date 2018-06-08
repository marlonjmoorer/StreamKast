package com.example.marlonmoorer.streamkast.api



import android.net.Uri
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


import com.example.marlonmoorer.streamkast.api.models.*

import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.github.magneticflux.rss.createRssPersister
import com.github.magneticflux.rss.namespaces.standard.elements.Channel
import com.github.magneticflux.rss.namespaces.standard.elements.Rss


import org.simpleframework.xml.core.Persister




//import org.simpleframework.xml.core.Persister




/**
 * Created by marlonmoorer on 3/21/18.
 */
class ItunesRepository {
    val service:ItunesService
    val serializer:Persister


    init {
        service= Retrofit.Builder()
                .baseUrl(ItunesService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesService::class.java)
        serializer= createRssPersister()

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

    fun parseFeed(url:String,page:String="1"): Channel?{
        try {


            val uri = Uri.parse(url)
                    .buildUpon()
                    .appendQueryParameter("page",page)
                    .build().toString()
            val xml= URL(uri).readText()
            return serializer.read(Rss::class.java, xml).channel
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
                "genreId" to genre.id,
                "entity" to "podcast"
        )
        return search(query)?.sortedByDescending {
            it.releaseDate
        }?.take(limit)

    }


}


