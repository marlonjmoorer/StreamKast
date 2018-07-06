package com.example.marlonmoorer.streamkast

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Notification
import android.app.Service
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.anko.image
import android.arch.persistence.room.Room
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.request.target.NotificationTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.example.marlonmoorer.streamkast.viewModels.BaseViewModel
import java.net.URL
import java.util.concurrent.Executors


/**
 * Created by marlonmoorer on 3/21/18.
 */
fun ImageView.load(url:String){
    Glide.with(context)
            .load(url)
            //.apply(RequestOptions().fitCenter())
            .into(this)

}
fun ImageView.load(id:Int) {
    this.image = ResourcesCompat.getDrawable(context.resources, id, null)
}

fun RemoteViews.loadImage(context: Context,target: NotificationTarget,url: String){
   Glide.with(context).asBitmap().load(url).into(target)
}

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(): T {

    if(!BaseViewModel::class.java.isAssignableFrom(T::class.java)){
        ViewModelProviders.of(this).get(T::class.java)
    }
    return ViewModelProviders.of(this, BaseViewModel.ViewModelFactory()).get(T::class.java)
}
inline fun <reified T : ViewModel> Fragment.createViewModel(): T {
    if(!BaseViewModel::class.java.isAssignableFrom(T::class.java)){
        ViewModelProviders.of(this.activity!!).get(T::class.java)
    }
    return ViewModelProviders.of(this.activity!!, BaseViewModel.ViewModelFactory()).get(T::class.java)
}

fun AppCompatActivity.addFragment(id:Int,fragment: Fragment){
    supportFragmentManager!!
   .beginTransaction()
   .add(id,fragment)
   .addToBackStack("over")
   .commit()
}
fun AppCompatActivity.replaceFragment(id:Int,fragment: Fragment){
    supportFragmentManager!!
    .beginTransaction()
    .replace(id,fragment)
    .commit()
}
fun Int.toTime():String{

    //val millis=this//.toLong()
    return StringBuffer()
            .append(String.format("%02d", this / (1000 * 60 * 60)))
            .append(":")
            .append(String.format("%02d", this % (1000 * 60 * 60) / (1000 * 60)))
            .append(":")
            .append(String.format("%02d", this % (1000 * 60 * 60) % (1000 * 60) / 1000))
            .toString()
}

fun View.fade(alpha:Float){
    this.animate()
            .alpha(alpha)
            .setDuration(0)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    when(this@fade.alpha){
                        in 0f..(.50f) ->this@fade.visibility=View.GONE
                        in (.50f)..1f->this@fade.visibility=View.VISIBLE
                    }
                }
            });

}
fun Context.load(url:String?,callback:(bitmap:Bitmap)->Unit){
     Glide.with(this).asBitmap().load(url).into(object :SimpleTarget<Bitmap>(){
         override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
           callback(resource)
         }
     })
}

fun Int.toByteSize():String{
    val unit = 1000
    if (this < unit) return "$this B"
    val exp = (Math.log(this.toDouble()) / Math.log(unit.toDouble())).toInt()
    val pre = ("kMGTPE")[exp - 1]
    return String.format("%.1f %sB", this / Math.pow(unit.toDouble(), exp.toDouble()), pre)
}



