package com.example.marlonmoorer.streamkast

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.example.marlonmoorer.streamkast.api.models.Episode



const val NOFICATION_ID=8888
const val CHANNEL_ID="9999"
val PLAY_PAUSE="PLAY_PAUSE"
val BACK="BACK"
val NEXT="NEXT"
val FF="FF"
val RR="RR"
val STOP="STOP"
val RESET="RESET"
class MediaService:Service(),MediaPlayer.OnPreparedListener  {

    inner class MediaBinder: Binder() {
        fun getService():MediaService {

            return this@MediaService
        }
    }

    val remoteView
            get()=RemoteViews(packageName, R.layout._notification).apply{
                setOnClickPendingIntent(R.id.play,
                        PendingIntent.getService(this@MediaService,1, buildIntent(PLAY_PAUSE),0))
            }

    val notification
            get()= NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TITLE")
            .setContentText("Text")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Tick")
            .setCustomContentView(remoteView)
            .build()
    override fun onBind(intent: Intent?): IBinder? {
        return MediaBinder()
    }
    fun buildIntent(actionName:String)=Intent(applicationContext,MediaService::class.java).apply {
        action=actionName
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       // startForeground(NOFICATION_ID,notification)
        when(intent!!.action){
            PLAY_PAUSE-> play_pause()
        }
        return START_STICKY
    }

    var mediaPlayer: MediaPlayer?=null
    var playlist:List<Episode> =ArrayList()
    var currentEpisode= MutableLiveData<Episode>()
    init {
        mediaPlayer= MediaPlayer()
        mediaPlayer?.setOnPreparedListener(this)
    }
    fun play_pause() = with(mediaPlayer!!){ if(isPlaying)pause()else start()}


    fun stop(){
        with(mediaPlayer!!){
            stop()
            reset()
        }
    }
    fun next(){


    }
    fun start(){
        val url= currentEpisode.value?.enclosure?.link
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(url!!)
        mediaPlayer?.prepareAsync()
        startForeground(NOFICATION_ID,notification)
    }
    fun setPlayList(list: List<Episode>){
        playlist=list
        currentEpisode.postValue(playlist.first())
    }

    override fun onPrepared(player: MediaPlayer) {
        player.start()
    }
}