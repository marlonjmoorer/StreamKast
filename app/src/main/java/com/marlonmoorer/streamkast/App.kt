package com.marlonmoorer.streamkast

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.marlonmoorer.streamkast.di.AppComponent
import com.marlonmoorer.streamkast.di.AppModule
import com.marlonmoorer.streamkast.di.DaggerAppComponent

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