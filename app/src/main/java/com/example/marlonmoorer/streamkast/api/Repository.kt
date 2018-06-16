package com.example.marlonmoorer.streamkast.api



import android.arch.lifecycle.LiveData
import android.net.Uri
import android.util.Log


import com.example.marlonmoorer.streamkast.api.models.*

import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.*
import javax.inject.Inject


//import org.simpleframework.xml.core.Persister




/**
 * Created by marlonmoorer on 3/21/18.
 */
class Repository @Inject constructor(database: KastDatabase,val itunesService: ItunesService, val rssParseService: RssToJsonService) {

    val subscriptions:SubscriptionDao
    val featuredItems:FeaturedDao
    init {
        subscriptions=database.SubscriptionDao()
        featuredItems= database.FeaturedDao()
    }

    fun search(query:Map<String, String>): SearchResults? {
      return this.itunesService.search(query).execute().body()
    }
    fun lookup(query:Map<String, String>): List<MediaItem>? {
        val response=this.itunesService.lookup(query).execute().body()
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
            val result=rssParseService.parseFeed(feedUrl).execute().body()
            return result
        }
       catch (ex:Exception){
           ex.printStackTrace()
           Log.e("help",ex.message)
       }
        return null
    }

    fun syncFeatured(id:String="0") = async{

       if (!featuredItems.hasRows(id)){
            val data=when(id){
                "0"->itunesService.topPodcast().execute().body()?.rss?.entries
                else->itunesService.topPodcastByGenre(id).execute().body()?.rss?.entries
            }
            val entries= data?.map {
                Featured().apply {
                    name=it.Name!!
                    podcastId=it.Id!!
                    imageUrl=it.Image!!
                    author=it.Artist!!
                    summary=it.Summary!!
                    genreId=id
                    //lastUpdateDate=it.LastUpdateDate!!
                }
            }
            entries?.let { featuredItems.insertAll(it) }
       }
    }
    fun getFeaturedPostcasts(id:String="0") = featuredItems.getByGenreId(id)


    fun topPodCast(limit:Int=10,genre: MediaGenre?=null): List<PodcastEntry>?{

        genre?.let {
            return itunesService.topPodcastByGenre(it.id,limit).execute().body()?.rss?.entries
        }
        return itunesService.topPodcast(limit).execute().body()?.rss?.entries
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

    fun isSubscribed(id: String)=subscriptions.exist(id)
    fun unsubscribe(podcastId:String)= with(subscriptions){
        val sub=  this.getById(podcastId)
        this.delete(sub)
    }
    fun subscribe(subscription: Subscription)=subscriptions.insert(subscription)






}


