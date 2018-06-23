package com.example.marlonmoorer.streamkast

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.databinding.Observable
import org.jetbrains.anko.startActivity
import java.net.URL
import android.support.v4.media.session.MediaSessionCompat
import android.content.BroadcastReceiver
import android.os.Bundle
import android.media.AudioManager
import android.content.IntentFilter
import android.content.ComponentName
import android.graphics.BitmapFactory
import android.media.session.MediaController
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle;

class MediaService:Service()  {

    companion object {
        const val NOTIFICATION_ID=8888
        const val CHANNEL_ID="9999"
        val PLAY="PLAY"
        val PAUSE="PAUSE"
        val BACK="BACK"
        val NEXT="NEXT"
        val FF="FF"
        val RR="RR"
        val STOP="STOP"
        val RESET="RESET"
        val MEDIAPLAYER="MEDIAPLAYER"

    }

    private var episodeModel: EpisodeModel
    private var mediaSession: MediaSessionCompat? = null
    private var controller: MediaControllerCompat.TransportControls?=null

    init {
        episodeModel= EpisodeModel()
        episodeModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, id: Int) {
                if(episodeModel.isPlaying==true) async { startForeground(NOTIFICATION_ID,notification) }
                else {
                    stopForeground(false)
                    updateNotification()
                }
            }
        })
    }


    inner class MediaBinder: Binder() {
        fun getModel(): EpisodeModel {

            return this@MediaService.episodeModel
        }
        val controller
            get() = this@MediaService.mediaSession?.controller
    }




    val notification:Notification
        get(){
            val play_pauseAction=if (episodeModel.isPlaying) playbackAction(1)
            else playbackAction(0);

            val largeIcon =
                    BitmapFactory.decodeStream(URL(episodeModel.thumbnail).openStream())
            val mediaStyle=MediaStyle()
                    .setMediaSession(mediaSession!!.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            val play_pause_icon= if(episodeModel.isPlaying) R.drawable.icons8_pause else R.drawable.icons8_play

            val notif = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setShowWhen(false)
                    .setStyle(mediaStyle)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                    .addAction(play_pause_icon, "pause", play_pauseAction)
                    .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2))
                    .setContentText(episodeModel.author)
                    .setContentTitle(episodeModel.title)
                    .setContentIntent(playbackAction(4))
                    .build()

            return notif
        }

    private fun playbackAction(actionNumber: Int): PendingIntent? {
        val playbackAction = Intent(this, MediaService::class.java).apply {
           action= when(actionNumber){
                0-> PLAY
                1-> PAUSE
                2-> NEXT
                3-> BACK
                else-> MEDIAPLAYER
           }
        }



        return PendingIntent.getService(this, actionNumber, playbackAction, 0)
    }


    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(noisyReceiver, filter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MediaBinder()
    }

    private fun initMediaSession() {
        mediaSession = MediaSessionCompat(applicationContext, "Tag")
        mediaSession?.let {
            it.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            it.setCallback(mediaSessionCallback)
            it.isActive=true
            controller =  it.controller.transportControls
        }
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val mReceiverComponent =   ComponentName(this,YourBroadcastReceiver::class.java)
        audioManager.registerMediaButtonEventReceiver(mReceiverComponent)
    }

    fun updateNotification()= async{
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID,notification)
    }
    fun buildIntent(actionName:String):PendingIntent{
        val intent=Intent(applicationContext,MediaService::class.java).apply {
            action=actionName
        }
        return PendingIntent.getService(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (episodeModel.isPlaying) {
              episodeModel.pause()
            }
        }
    }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            super.onPlay()
            if(successfullyRetrievedAudioFocus()){
                episodeModel.play()
            }
        }

        override fun onPause() {
            super.onPause()
            episodeModel.pause()
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)

        }



    }

    private fun successfullyRetrievedAudioFocus(): Boolean {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = audioManager.requestAudioFocus(episodeModel, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        return result == AudioManager.AUDIOFOCUS_GAIN
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent!!.action){

            PLAY-> controller?.play()
            PAUSE-> controller?.pause()
            FF-> controller?.seekTo(episodeModel.position+30000L)
            RR->controller?.seekTo(episodeModel.position+30000L)
            STOP->controller?.stop()
            MEDIAPLAYER->startActivity<MediaPlayerActivity>()
            else->  async{
                startForeground(NOTIFICATION_ID,notification)
            }

        }
        return START_STICKY
    }


    // Constructor is mandatory
    class YourBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val intentAction = intent.action

            // other stuff you want to do
        }
    }


}

