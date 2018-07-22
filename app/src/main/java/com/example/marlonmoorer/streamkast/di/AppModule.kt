package com.example.marlonmoorer.streamkast.di

import android.content.Context
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.ItunesService
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.RssToJsonService
import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(context: Context) {

    private val context:Context

    init {
        this.context=context
    }

    @Provides
    @Singleton
    fun provideAppContext():Context{
        return this.context
    }

    @Provides
    @Singleton
    fun provideRepository(database: KastDatabase,itunesService: ItunesService, okHttpClient: OkHttpClient):Repository{
        return Repository(database,itunesService,okHttpClient,context.getSharedPreferences(context.packageName,0))
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context):KastDatabase{
        return KastDatabase.get(context)
    }

    @Provides
    @Singleton
    fun provideItunesService(factory: GsonConverterFactory):ItunesService{
        return Retrofit.Builder()
                .baseUrl(ItunesService.baseUrl)
                .addConverterFactory(factory)
                .build()
                .create(ItunesService::class.java)
    }

    @Provides
    @Singleton
    fun provideRssParseService(context: Context,factory: GsonConverterFactory):RssToJsonService{
        RssToJsonService.setApiKey(context.getString(R.string.rss2jsonApKey))
        return Retrofit.Builder()
                .baseUrl(RssToJsonService.baseUrl)
                .addConverterFactory(factory)
                .build()
                .create(RssToJsonService::class.java)
    }

    @Provides
    @Singleton
    fun provideGsonFactory()= GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideOkHttpClient()= OkHttpClient()

    @Provides
    @Singleton
    fun proviedFetch(context: Context): Fetch {
        val fetchConfiguration = FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(5)
                .build()
       return Fetch.getInstance(fetchConfiguration)
    }



}