package com.example.marlonmoorer.streamkast

import android.app.Activity
import android.app.Service
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
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
fun ImageView.load(id:Int) {
    this.image = ResourcesCompat.getDrawable(context.resources, id, null)
}

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(): T {
 return ViewModelProviders.of(this).get(T::class.java)
}
inline fun <reified T : ViewModel> Fragment.createViewModel(): T {
    return ViewModelProviders.of(this.activity!!).get(T::class.java)
}

//inline fun <reified T : ViewModel> Service.createViewModel(): T {
//    return ViewModelProviders.of(this).get(T::class.java)
//}