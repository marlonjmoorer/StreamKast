package com.example.marlonmoorer.streamkast

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView



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
    @BindingAdapter("src")
    fun loadImage(view: ImageView, image:Bitmap?){
        view.setImageBitmap(image)
    }



    @JvmStatic
    @BindingAdapter("progress")
    fun setProgress(view: SeekBar, progress:Int?){
       progress?.let { view.progress=progress }
    }
    @JvmStatic
    @BindingAdapter("max")
    fun setMax(view: SeekBar,max:Int?){
       max?.let {  view.max=max}
    }


    @JvmStatic
    @BindingAdapter("href")
    fun setLink(view: TextView, url: String?){
       url?.let {
           view.text= Html.fromHtml("<a href='${url}'>View</a>")
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
    @BindingAdapter("visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.setVisibility(if (value) View.VISIBLE else View.GONE)
    }

}

