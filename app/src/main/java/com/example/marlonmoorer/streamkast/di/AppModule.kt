package com.example.marlonmoorer.streamkast.di

import android.content.Context
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.data.KastDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(context: Context) {

    private val context:Context

    init {
        this.context=context
    }

    @Provides
    @Singleton
    fun provideRepository():Repository{
        return Repository()
    }

    @Provides
    @Singleton
    fun provideDatabase():KastDatabase{
        return KastDatabase.get(this.context)
    }

}