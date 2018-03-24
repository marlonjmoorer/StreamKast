package com.example.marlonmoorer.streamkast.api

import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.FeedResult
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.github.magneticflux.rss.createRssPersister
import com.github.magneticflux.rss.namespaces.standard.elements.Rss
import okhttp3.internal.Internal
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


    fun search(query:Map<String, String>): List<MediaItem>? {
       // if (!qurey.containsKey("limit")) qurey.set("limit",10)
        var response=this.service.search(query).execute().body()
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

    fun topPodCast(limit:Int=10): List<MediaItem>? {
        val items= this.rssService.topPodcast(limit).execute().body()
        return items?.feed?.results?.map {
            MediaItem().apply {
                kind= it.kind
                collectionName=it.name
                artistName=it.artistName
                artworkUrl100=it.artworkUrl100
                collectionId= it.id?.toLong()
                feedUrl=it.url
                genreIds= it.genres?.map { it.genreId!! }
                artistId=it.artistId
            }
        }
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