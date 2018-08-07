package com.example.marlonmoorer.streamkast.api



import android.app.DownloadManager
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.example.marlonmoorer.streamkast.*


import com.example.marlonmoorer.streamkast.api.models.*


import com.example.marlonmoorer.streamkast.data.*

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.anko.doAsync
import org.joda.time.DateTime

import retrofit2.Call
import java.io.IOException
import javax.inject.Inject


class Repository @Inject constructor(database: KastDatabase, val itunesService: ItunesService, val httpClient: OkHttpClient, val preferences: SharedPreferences) {

    val subscriptions:SubscriptionDao
    val featuredItems:FeaturedDao
    val savedEpisodes:EpisodeDao
    var history:HistoryDao
    var exceptionHanlder:LiveData<Exception>


    init {
        subscriptions=database.SubscriptionDao()
        featuredItems= database.FeaturedDao()
        savedEpisodes=database.EpisodeDao()
        history=database.HistoryDao()
        exceptionHanlder= MutableLiveData()
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
        val request = Request.Builder()
                .url(feedUrl)
                .build()
        httpClient.newCall(request).enqueue(object :okhttp3.Callback{
            override fun onResponse(call: okhttp3.Call?, response: Response) {
                val result= response.body()?.byteStream()?.use {
                    Utils.parse(it)
                }
                channel.postValue(result)
            }

            override fun onFailure(call: okhttp3.Call?, e: IOException) {
                throw e
            }
        })
        return channel
    }

    private fun syncFeatured(id:String) {
        val key= "sync${id}"
        val lastSyncDate = preferences.getLong(key,0)
        val hoursAgo= DateTime.now().minusHours(3).millis
        if (lastSyncDate < hoursAgo) {

            val call=when (id) {
                MediaGenre.Featured.id -> itunesService.topPodcast()
                else -> itunesService.topPodcastByGenre(id)
            }
            val feed=call.execute().body()
            val entries = feed?.rss?.entries?.map {
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
                featuredItems.clearGenreItems(id)
                featuredItems.insertAll(it)
            }

            preferences.edit().putLong(key,System.currentTimeMillis()).apply()
        }
    }
    fun getFeaturedPostcasts(id: String): LiveData<List<Featured>> {
        doAsync {
            syncFeatured(id)
        }
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







