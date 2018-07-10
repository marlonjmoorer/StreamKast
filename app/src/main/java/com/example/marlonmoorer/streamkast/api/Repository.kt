package com.example.marlonmoorer.streamkast.api



import android.util.Log
import com.example.marlonmoorer.streamkast.Utils


import com.example.marlonmoorer.streamkast.api.models.*
import com.example.marlonmoorer.streamkast.api.models.rss.Channel

import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.*
import javax.inject.Inject


class Repository @Inject constructor(database: KastDatabase,val itunesService: ItunesService) {

    val subscriptions:SubscriptionDao
    val featuredItems:FeaturedDao
    val savedEpisodes:EpisodeDao

    init {
        subscriptions=database.SubscriptionDao()
        featuredItems= database.FeaturedDao()
        savedEpisodes=database.EpisodeDao()
    }

    fun search(query:Map<String, String>): SearchResults? {
      return this.itunesService.search(query).execute().body()
    }
    private fun lookup(query:Map<String, String>): List<Podcast>? {
        val response=this.itunesService.lookup(query).execute().body()
        response?.let {
            return  it.results
        }
        return emptyList()
    }
    fun getPodcastById(id:String):Podcast?{
        return lookup(mapOf("id" to id))?.firstOrNull()
    }

    fun parseFeed(feedUrl:String,page:String="1"):Channel?{
        try {
            val result=Utils.parseFeed(feedUrl)
            return result
        }
       catch (ex:Exception){
           ex.printStackTrace()
           Log.e("help",ex.message)
       }
        return null
    }

    fun syncFeatured(id:String) = async{

      // if (!featuredItems.hasRows(id)){
            featuredItems.clearGenreItems(id)
            val data=when(id){
                MediaGenre.Featured.id->itunesService.topPodcast()
                else->itunesService.topPodcastByGenre(id)
            }.execute().body()?.rss?.entries

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
       //}
    }
    fun getFeaturedPostcasts(id:String) = featuredItems.getByGenreId(id)


    fun getShowsByGenre(genre: MediaGenre,limit: Int=10): List<Podcast>? {
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


