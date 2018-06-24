package com.example.marlonmoorer.streamkast

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat

class MediaModel(val controller:MediaControllerCompat): BaseObservable() {

    private var metaData:MediaMetadataCompat?=null
    private val callback=object :MediaControllerCompat.Callback(){
        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            metaData= metadata
            notifyChange()
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            isPlaying=state?.state==PlaybackStateCompat.STATE_PLAYING
            position= state?.position?.toInt()
            notifyPropertyChanged(BR.playing)
            notifyPropertyChanged(BR.elapsed)
        }

    }
    init {
        controller.registerCallback(callback)
    }

    val transportControls
            get() = controller.transportControls



    val author
        @Bindable get() = metaData?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
    @Bindable
    var isPlaying = false

    val title
        @Bindable get() = metaData?.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
    val thumbnail
        @Bindable get() = metaData?.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

    val elapsed
        @Bindable get() = position?.toTime()

    val duration
        @Bindable get() = length?.toTime()


    val length
        @Bindable get() = metaData?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)?.toInt()

    @Bindable
    var position:Int?=0

    fun play(){
        transportControls.play()
        notifyPropertyChanged(BR.playing)
    }
    fun pause(){
        transportControls.pause()
        notifyPropertyChanged(BR.playing)
    }

    fun togglePlayback(){
        if (isPlaying) pause() else play()
    }

    fun seekTo(position: Int) =transportControls.seekTo(position.toLong())


}