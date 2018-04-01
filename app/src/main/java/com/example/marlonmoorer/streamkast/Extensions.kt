package com.example.marlonmoorer.streamkast

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by marlonmoorer on 3/21/18.
 */
fun ImageView.load(url:String){
    Glide.with(context)
            .load(url)
            //.apply(RequestOptions().fitCenter())
            .into(this);
}
fun Any.async(fn:()->Unit):Thread{
   val thread= Thread({
        fn()
    })
    thread.start()
    return thread
}