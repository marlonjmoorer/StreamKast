package com.example.marlonmoorer.streamkast.di

import android.app.DownloadManager
import android.content.Context
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.ItunesService
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.RssToJsonService
import com.example.marlonmoorer.streamkast.data.KastDatabase

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
    fun provideRepository(database: KastDatabase,itunesService: ItunesService, okHttpClient: OkHttpClient,downloadManager: DownloadManager):Repository{


        return Repository(database,itunesService,okHttpClient,context.getSharedPreferences(context.packageName,0),downloadManager)
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
    fun provideDownloadManager(context: Context):DownloadManager{
        return  context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }




}