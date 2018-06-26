package com.example.marlonmoorer.streamkast

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat

class MediaModel(val controller:MediaControllerCompat): BaseObservable() {

    private var metadata:MediaMetadataCompat?=null
    private var state:PlaybackStateCompat?=null

//    private val callback=object :MediaControllerCompat.Callback(){
//        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
//            this@MediaModel.metadata= metadata
//            notifyChange()
//        }
//
//        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
//            this@MediaModel.state=state
//            notifyPropertyChanged(BR.playing)
//            notifyPropertyChanged(BR.elapsed)
//            notifyPropertyChanged(BR.position)
//        }
//
//    }
    init {
       // controller.registerCallback(callback)
    }

    val transportControls
            get() = controller.transportControls



    val author
        @Bindable get() = this.metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

    val playing
        @Bindable get() = this.state?.state==PlaybackStateCompat.STATE_PLAYING

    val title
        @Bindable get() = this.metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
    val thumbnail
        @Bindable get() = this.metadata?.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

    val elapsed
        @Bindable get() = this.position?.toTime()

    val duration
        @Bindable get() = this.length?.toTime()


    val length
        @Bindable get() = this.metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)?.toInt()


    val position
     @Bindable get() = this.state?.position?.toInt()

    fun play(){
        this.transportControls.play()
       // notifyPropertyChanged(BR.playing)
    }
    fun pause(){
        this.transportControls.pause()
        //notifyPropertyChanged(BR.playing)
    }

    fun togglePlayback(){
        if (this.playing) pause() else play()
        //notifyPropertyChanged(BR.playing)
    }

    fun seekTo(position: Int) = this.transportControls.seekTo(position.toLong())


}