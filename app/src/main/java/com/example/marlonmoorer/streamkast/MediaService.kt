package com.example.marlonmoorer.streamkast

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.example.marlonmoorer.streamkast.api.models.Episode
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable


const val NOTIFICATION_ID=8888
const val CHANNEL_ID="9999"
val PLAY_PAUSE="PLAY_PAUSE"
val BACK="BACK"
val NEXT="NEXT"
val FF="FF"
val RR="RR"
val STOP="STOP"
val RESET="RESET"
val DISMISS="DISMISS"
class MediaService:Service()  {

    private var episodeModel:EpisodeModel
    init {
        episodeModel=EpisodeModel()
        episodeModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, id: Int) {
                if(episodeModel.isPlaying==true) startForeground(NOTIFICATION_ID,notification)
                else {
                    stopForeground(false)
                    updateNotification()
                }
            }
        })
    }

    inner class MediaBinder: Binder() {
        fun getModel():EpisodeModel {
            startForeground(NOTIFICATION_ID,notification)
            return this@MediaService.episodeModel
        }
    }


    val remoteView
        get()=RemoteViews(packageName, R.layout._notification).apply{
            setOnClickPendingIntent(R.id.play,buildIntent(PLAY_PAUSE))
            val buttonResource=if(episodeModel.isPlaying) R.drawable.icons8_pause else R.drawable.icons8_play
            setImageViewResource(R.id.play,buttonResource)
        }

    val notification
            get()= NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TITLE")
            .setContentText("Text")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(remoteView)
            .setDeleteIntent(buildIntent(DISMISS))
            .build()

    override fun onBind(intent: Intent?): IBinder? {
        return MediaBinder()
    }

    fun showNotification(){
        startForeground(NOTIFICATION_ID, notification)
    }
    fun hideNotification(){
        stopForeground(true)
    }

    fun updateNotification(){
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID,notification)
    }
    fun buildIntent(actionName:String):PendingIntent{
        val intent=Intent(applicationContext,MediaService::class.java).apply {
            action=actionName
        }
        return PendingIntent.getService(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       // startForeground(NOTIFICATION_ID,notification)
        when(intent!!.action){
            PLAY_PAUSE-> episodeModel.play_pause()
            DISMISS -> stopSelf()
            else->  startForeground(NOTIFICATION_ID,notification)
        }
        return START_STICKY
    }


    class EpisodeModel : BaseObservable(),MediaPlayer.OnPreparedListener {

        private  var _episode: Episode? = null
        private var mediaPlayer: MediaPlayer


        init {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setOnPreparedListener(this)
        }

        val isPlaying
            @Bindable get() = this.mediaPlayer.isPlaying
        val title
            @Bindable get() = _episode?.title
        val thumbnail
            @Bindable get() = _episode?.thumbnail


        fun setEpisode(episode: Episode){
            this._episode=episode
            notifyChange()
            notifyPropertyChanged(NOTIFICATION_ID)
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

    }


}

