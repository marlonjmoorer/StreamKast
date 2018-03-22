package com.example.marlonmoorer.streamkast.api

import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.FeedResult
import com.github.magneticflux.rss.createRssPersister
import com.github.magneticflux.rss.namespaces.standard.elements.Rss
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


/**
 * Created by marlonmoorer on 3/21/18.
 */
class ItunesRepository {
    val service:ItunesService
    val rssService:ItunesRssService

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
    }


    fun Search(term:String): List<MediaItem>? {
       var response=this.service.search(mapOf("term" to term)).execute().body()
       response?.let {
          return  it.results
       }
        return emptyList()
    }

    fun ParseFeed(url:String):Rss{
        val persister = createRssPersister()
        val input=URL(url).readText()
        return persister.read(Rss::class.java,input)
    }

    fun topPodCast(): List<FeedResult>? {
        val items= this.rssService.topPodcast(10).execute().body()
        return items?.feed?.results
    }

}