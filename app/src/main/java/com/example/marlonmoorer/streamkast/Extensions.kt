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
import com.airbnb.paris.Paris
import com.bumptech.glide.request.target.NotificationTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.example.marlonmoorer.streamkast.viewModels.BaseViewModel
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import javax.xml.parsers.DocumentBuilderFactory


/**
 * Created by marlonmoorer on 3/21/18.
 */
fun ImageView.loadAsBitmap(url:String){
    Glide.with(context)
            .load(url)
            //.apply(RequestOptions().fitCenter())
            .into(this)

}
fun ImageView.loadAsBitmap(id:Int) {
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

fun AppCompatActivity.addFragment(id:Int,fragment: Fragment,withAnimation:Boolean=false){
    val transaction= supportFragmentManager!!
   .beginTransaction().apply {
        if(withAnimation) {
            setCustomAnimations(0,0,R.anim.enter_right,R.anim.exit_right)
        }
    }.add(id,fragment)
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
 fun View.applyStyle(id: Int){
     Paris.style(this).apply(id)
 }

fun Context.loadAsBitmap(url:String?, callback:(bitmap:Bitmap)->Unit){
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
fun String.toDate():Date?{
    val df = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
    val date: Date
    try {
        date = df.parse(this)
        return date
    }catch(ex: ParseException){
        return null
    }
}
fun String.toDateTime(): Date? {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(this)
}
fun Date.toDateString(): String? {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this)
}


fun URL.asXmlDoc(): Document {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.openStream())
}
operator fun Element.get(key:String):Element{
    return this.getElementsByTagName(key).item(0) as Element
}
operator fun Document.get(key:String):Element {
    return this.getElementsByTagName(key).item(0) as Element
}


fun Element.text(key:String): String? {
    if(!this.has(key)){
        return  null
    }
    return this.getElementsByTagName(key).item(0).textContent
}

fun Element.getList(key:String): NodeList {
    return this.getElementsByTagName(key)
}
fun Element.has(key: String):Boolean{
    return this.getElementsByTagName(key).length>0
}

val NodeList.Nodes:List<Node>
    get(){
       return mutableListOf<Node>().also{list->
           for (i in 0..length-1) {
               list.add(item(i))
           }
       }
    }
inline fun  NodeList.forEach(action: (Node) -> Unit){
    for (i in 0..this.length) {
        val element=  this.item(i)
        element?.let(action)
    }
}
inline fun < R> NodeList.map(transform: (Node) -> R): List<R> {
    val list= mutableListOf<R>()
    for (i in 0..this.length) {
        val element=this.item(i)
        element?.let{
            list.add(transform(element))
        }
    }
    return list
}

 fun <T> Call<T>.onResponse(callback:(T)->Unit){
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) {
            response.body()?.let(callback)
        }
        override fun onFailure(call: Call<T>?, t: Throwable?) {
           throw  t!!
        }
    })
}



