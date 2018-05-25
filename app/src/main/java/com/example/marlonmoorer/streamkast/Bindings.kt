package com.example.marlonmoorer.streamkast

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.example.marlonmoorer.streamkast.api.models.MediaGenre

object ImageAdapter{

    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(view: ImageView, imageUrl: String)= view.load(imageUrl)

}

interface ISelectHandler{
    fun onPodcastSelect(id:String){

    }
    fun onEpisodeSelect(id:String){

    }
    fun onGenreSelect(genre: MediaGenre){

    }
}