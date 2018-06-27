package com.example.marlonmoorer.streamkast

import android.app.Application
import android.util.Log
import com.example.marlonmoorer.streamkast.di.AppComponent
import com.example.marlonmoorer.streamkast.di.AppModule
import com.example.marlonmoorer.streamkast.di.DaggerAppComponent
import java.util.concurrent.Executors


private val BACKGROUND_THREAD= Executors.newSingleThreadExecutor()


fun async(fn:()->Unit){
    val future = BACKGROUND_THREAD.submit(fn)
    try {
        future.get()
    }catch (ex:Exception){
        Log.e("ERR",ex.message)
    }

}

