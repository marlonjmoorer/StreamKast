package com.example.marlonmoorer.streamkast

import android.app.Application
import com.example.marlonmoorer.streamkast.di.AppComponent
import com.example.marlonmoorer.streamkast.di.AppModule
import com.example.marlonmoorer.streamkast.di.DaggerAppComponent

class App : Application(){

    companion object {
        private var _component: AppComponent?=null
        val component
            get() = _component
    }

    override fun onCreate() {
        super.onCreate()
        _component= DaggerAppComponent.builder()
                .appModule(AppModule(this.applicationContext))
                .build()
    }
}