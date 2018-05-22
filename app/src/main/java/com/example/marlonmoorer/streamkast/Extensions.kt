package com.example.marlonmoorer.streamkast

import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.anko.image

/**
 * Created by marlonmoorer on 3/21/18.
 */
fun ImageView.load(url:String){
    Glide.with(context)
            .load(url)
            //.apply(RequestOptions().fitCenter())
            .into(this);
}
fun ImageView.load(id:Int){
  this.image= ResourcesCompat.getDrawable(context.resources,id,null)
}
fun Any.async(fn:()->Unit):Thread{
   val thread= Thread({
        fn()
    })
    thread.start()
    return thread
}