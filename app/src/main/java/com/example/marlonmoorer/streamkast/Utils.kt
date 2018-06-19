package com.example.marlonmoorer.streamkast

import android.app.Application
import com.example.marlonmoorer.streamkast.di.AppComponent
import com.example.marlonmoorer.streamkast.di.AppModule
import com.example.marlonmoorer.streamkast.di.DaggerAppComponent
import java.util.concurrent.Executors


private val BACKGROUND_THREAD= Executors.newSingleThreadExecutor()


fun async(fn:()->Unit)=BACKGROUND_THREAD.execute(fn)

