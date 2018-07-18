package com.example.marlonmoorer.streamkast.api



import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.util.Log
import com.example.marlonmoorer.streamkast.*


import com.example.marlonmoorer.streamkast.api.models.*
import com.example.marlonmoorer.streamkast.api.models.rss.Channel


import com.example.marlonmoorer.streamkast.data.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import retrofit2.Call
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class Repository @Inject constructor(database: KastDatabase, val itunesService: ItunesService, val httpClient: OkHttpClient, val preferences: SharedPreferences) {

    val subscriptions:SubscriptionDao
    val featuredItems:FeaturedDao
    val savedEpisodes:EpisodeDao
    var history:HistoryDao
    private val LASTUPDATE="lastUpdateDate"

    init {
        subscriptions=database.SubscriptionDao()
        featuredItems= database.FeaturedDao()
        savedEpisodes=database.EpisodeDao()
        history=database.HistoryDao()
    }

    fun search(query:Map<String, String>): Call<SearchResults> {
      return this.itunesService.search(query)
    }


    private fun lookup(query:Map<String, String>): List<Podcast>? {
        val response=this.itunesService.lookup(query).execute().body()
        response?.let {
            return  it.results
        }
        return emptyList()
    }
    fun getPodcastById(id:String): MutableLiveData<Podcast> {
        val podcast=MutableLiveData<Podcast>()
        doAsync{
           podcast.postValue(lookup(mapOf("id" to id))?.firstOrNull())
        }
        return podcast
    }

    fun parseFeed(feedUrl:String):LiveData<Channel>{
        val channel=MutableLiveData<Channel>()
        doAsync {
            try {
                val request = Request.Builder()
                        .url(feedUrl)
                        .build()

                val response = httpClient.newCall(request).execute()
                val result= response.body()?.byteStream()?.let {
                    Utils.parseFeed(it)
                }
                channel.postValue(result)
            }
            catch (ex:Exception){
                ex.printStackTrace()
                Log.e("help",ex.message)
            }
        }
        return channel
    }

    private fun syncFeatured(id:String) {
        val key= "${LASTUPDATE}_${id}"
        val lastSyncDate = preferences.getString(key, null)
        val hoursAgo=Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)).toDateString()!!
        if (lastSyncDate==null||lastSyncDate < hoursAgo) {

            val call=when (id) {
                MediaGenre.Featured.id -> itunesService.topPodcast()
                else -> itunesService.topPodcastByGenre(id)
            }

            call.onResponse {feed->
                val entries = feed.rss?.entries?.map {
                    Featured().apply {
                        name = it.Name!!
                        podcastId = it.Id!!
                        imageUrl = it.Image!!
                        author = it.Artist!!
                        summary = it.Summary!!
                        genreId = id
                    }
                }
                entries?.let {
                    doAsync {
                        featuredItems.clearGenreItems(id)
                        featuredItems.insertAll(it)
                    }
                }
                preferences.edit().putString(key,Date().toDateString()).apply()
            }
        }
    }
    fun getFeaturedPostcasts(id: String): LiveData<List<Featured>> {
        syncFeatured(id)
        return featuredItems.getByGenreId(id)
    }


    fun getShowsByGenre(genreId:String): LiveData<List<Podcast>> {
        val query = mapOf(
                "term" to "podcast",
                "genreId" to genreId,
                "entity" to "podcast",
                "limit" to "30"
        )
        val results=MutableLiveData<List<Podcast>>()
        this.itunesService.search(query).onResponse{result ->
                results.postValue(result.results)
        }
        return results

    }

    fun isSubscribed(id: String) = subscriptions.exist(id)
    fun unsubscribe(podcastId: String) = with(subscriptions) {
        val sub = this.getById(podcastId)
        this.delete(sub)
    }

    fun subscribe(subscription: Subscription) = subscriptions.insert(subscription)


}







