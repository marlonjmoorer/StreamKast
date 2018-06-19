package com.example.marlonmoorer.streamkast

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.media.MediaPlayer
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.api.models.Episode

class EpisodeModel : BaseObservable(), MediaPlayer.OnPreparedListener {

    private  var _episode: Episode? = null
    private var mediaPlayer: MediaPlayer


    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener(this)

    }
    val author
         @Bindable get() =  _episode?.author
    val isPlaying
        @Bindable get() = this.mediaPlayer.isPlaying
    val title
        @Bindable get() = _episode?.title
    val thumbnail
        @Bindable get() = _episode?.thumbnail

    val elapsed
        @Bindable get() = mediaPlayer.currentPosition.toTime()

    val duration
        @Bindable get() = mediaPlayer.duration.toTime()


    val length
        @Bindable get() = mediaPlayer.duration
    val position
        @Bindable get() = mediaPlayer.currentPosition
    fun setEpisode(episode: Episode){
        this._episode=episode
        notifyChange()
    }

    fun play(){
        mediaPlayer.start()
        notifyPropertyChanged(BR.playing)
    }
    fun pause(){
        mediaPlayer.pause()
        notifyPropertyChanged(BR.playing)
    }
    fun play_pause() = with(mediaPlayer){
        if(isPlaying) pause()
        else start()
        notifyPropertyChanged(BR.playing)
    }
    override fun onPrepared(player: MediaPlayer) {
        player.start()
        notifyChange()
    }

    fun prepare(){
        val url= _episode?.enclosure?.link
        with(mediaPlayer){
            reset()
            setDataSource(url)
            prepareAsync()
        }
    }


    fun stop(){
        with(mediaPlayer){
            stop()
            reset()
            notifyChange()
        }
    }

    fun seekTo(position: Int)=mediaPlayer.seekTo(position)

    var listener:SeekBar.OnSeekBarChangeListener?=null


}