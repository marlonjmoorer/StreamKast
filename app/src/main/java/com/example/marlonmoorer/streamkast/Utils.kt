package com.example.marlonmoorer.streamkast

import java.util.concurrent.Executors


private val BACKGROUND_THREAD= Executors.newSingleThreadExecutor()

fun async(fn:()->Unit)=BACKGROUND_THREAD.execute(fn)

