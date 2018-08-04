package com.example.marlonmoorer.streamkast

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.marlonmoorer.streamkast.di.AppComponent
import com.example.marlonmoorer.streamkast.di.AppModule
import com.example.marlonmoorer.streamkast.di.DaggerAppComponent

import net.danlew.android.joda.JodaTimeAndroid
import org.jetbrains.anko.longToast


class App : Application() {


    companion object {
        private var appComponent: AppComponent?=null
        val component
            get() = appComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent= DaggerAppComponent.builder()
                .appModule(AppModule(this.applicationContext))
                .build()
        JodaTimeAndroid.init(this)
    }
}