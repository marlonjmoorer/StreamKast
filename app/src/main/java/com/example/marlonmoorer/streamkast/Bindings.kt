package com.example.marlonmoorer.streamkast

import android.databinding.BindingAdapter
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.marlonmoorer.streamkast.api.models.Episode

import com.example.marlonmoorer.streamkast.api.models.MediaGenre


object BindingAdapters{


    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(view: ImageView, resourceId:Int?){
        resourceId?.let {
            view.load(it)
        }
    }

    @JvmStatic
    @BindingAdapter("src")
    fun loadImageUrl(view: ImageView, imageUrl: String?){
        imageUrl?.let {
            view.load(it)
        }
    }
    @JvmStatic
    @BindingAdapter("href")
    fun setLink(view: TextView, url: String?){
       url?.let {
           view.text= Html.fromHtml("<a href='${url}'>view</a>")
           view.movementMethod= LinkMovementMethod.getInstance()
       }
    }
    @JvmStatic
    @BindingAdapter("html")
    fun setHtml(view: TextView, text: String?){
        text?.let {
            view.text= Html.fromHtml(text)
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.setVisibility(if (value) View.VISIBLE else View.GONE)
    }

}

interface ISelectHandler{
    fun onPodcastSelect(id:String){}
    fun onEpisodeSelect(episode: Episode){}
    fun onGenreSelect(genre: MediaGenre){}
    fun queueEpisode(episode: Episode){}
}