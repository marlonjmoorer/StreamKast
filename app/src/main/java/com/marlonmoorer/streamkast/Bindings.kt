package com.marlonmoorer.streamkast

import android.app.DownloadManager
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.PlaybackStateCompat
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import java.util.*


object BindingAdapters{


    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(view: ImageView, resourceId:Int?){
        resourceId?.let {
            view.loadAsBitmap(it)
        }
    }

    @JvmStatic
    @BindingAdapter("src")
    fun loadImageUrl(view: ImageView, imageUrl: String?){
        imageUrl?.let {
            view.loadAsBitmap(it)
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

    @JvmStatic
    @BindingAdapter("date")
    fun setDate(view:TextView,date: Date?){
        view.text=date?.toDateString()
    }

    @JvmStatic
    @BindingAdapter(value = ["playImage", "pauseImage","playState"], requireAll = false)
    fun setStatus(view: ImageButton, playImage:Drawable, pauseImage:Drawable, playState:Int){

        when(playState) {
            PlaybackStateCompat.STATE_PLAYING -> {
                view.setImageDrawable(pauseImage)
            }
            PlaybackStateCompat.STATE_BUFFERING-> {

            }
            PlaybackStateCompat.STATE_PAUSED,
            PlaybackStateCompat.STATE_NONE,
            PlaybackStateCompat.STATE_STOPPED-> {
                view .setImageDrawable(playImage)
            }
        }

    }

    @JvmStatic
    @BindingAdapter("status")
    fun setStatus(view: TextView, status:Int){
        val ctx= view.context
        view.text=when(status){
            DownloadManager.STATUS_PENDING->ctx.getString(R.string.staus_pending)
            DownloadManager.STATUS_RUNNING->ctx.getString(R.string.staus_downloading)
            DownloadManager.STATUS_SUCCESSFUL->ctx.getString(R.string.status_ready)
            else->""
        }
    }

}

