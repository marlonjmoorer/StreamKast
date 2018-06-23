package com.example.marlonmoorer.streamkast

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.media.AudioManager
import android.media.MediaPlayer
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.api.models.Episode

class EpisodeModel : BaseObservable(), MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {

    private  var _episode: Episode? = null
    private var mediaPlayer: MediaPlayer


    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnBufferingUpdateListener{p,i->

        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setVolume(1.0f, 1.0f)
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
            pause()
            stop()
            reset()
            release()
        }
    }

    fun seekTo(position: Int)=mediaPlayer.seekTo(position)

    var listener:SeekBar.OnSeekBarChangeListener?=null

    override fun onAudioFocusChange(id: Int) {
        when (id) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                mediaPlayer.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer.setVolume(0.3f, 0.3f)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                    if (!mediaPlayer.isPlaying()&&_episode!==null) {
                        mediaPlayer.start()
                    }
                    mediaPlayer.setVolume(1.0f, 1.0f)
                }
            }
        }



}