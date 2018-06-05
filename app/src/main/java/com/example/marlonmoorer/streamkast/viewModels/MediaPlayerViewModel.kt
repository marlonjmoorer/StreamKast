package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.media.MediaPlayer
import com.example.marlonmoorer.streamkast.api.models.Episode

class MediaPlayerViewModel:ViewModel(),MediaPlayer.OnPreparedListener {

    var mediaPlayer:MediaPlayer?=null
    var playlist:List<Episode> =ArrayList()
    var currentIndex=0
    init {
        mediaPlayer= MediaPlayer()
        mediaPlayer?.setOnPreparedListener(this)
    }
    fun play() = mediaPlayer?.start()
    fun pause() = mediaPlayer?.pause()
    fun stop(){
        with(mediaPlayer!!){
            stop()
            reset()
        }
    }
    fun next(){


    }
    fun start(){
        val url= playlist[currentIndex].enclosure?.url
        mediaPlayer?.setDataSource(url!!)
        mediaPlayer?.prepareAsync()
    }
    fun setPlayList(list: List<Episode>){
        playlist=list
        this.start()
    }

    override fun onPrepared(player: MediaPlayer) {
       player.start()
    }
}