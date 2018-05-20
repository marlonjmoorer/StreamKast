package com.example.marlonmoorer.streamkast.adapters

import android.databinding.BindingAdapter
import android.databinding.DataBindingComponent
import android.widget.ImageView
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.load

/**
 * Created by marlonmoorer on 4/1/18.
// */
object ImageAdapter{

    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(view: ImageView, imageUrl: String)= view.load(imageUrl)

}