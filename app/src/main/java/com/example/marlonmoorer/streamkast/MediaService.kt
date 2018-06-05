package com.example.marlonmoorer.streamkast

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.marlonmoorer.streamkast.api.models.Episode

class MediaService:Service(),MediaPlayer.OnPreparedListener  {

    inner class MediaBinder: Binder() {
        fun getService():MediaService {
            return this@MediaService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MediaBinder()
    }

    var mediaPlayer: MediaPlayer?=null
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