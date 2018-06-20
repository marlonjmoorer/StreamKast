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
import android.widget.RemoteViews
import android.databinding.Observable
import android.net.Uri
import com.bumptech.glide.request.target.NotificationTarget
import org.jetbrains.anko.startActivity
import java.net.URL



class MediaService:Service()  {

    companion object {
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
        val MEDIAPLAYER="MEDIAPLAYER"
    }

    private var episodeModel: EpisodeModel
    init {
        episodeModel= EpisodeModel()
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
        fun getModel(): EpisodeModel {
          //  startForeground(NOTIFICATION_ID,notification)
            return this@MediaService.episodeModel
        }
    }



    val notification:Notification
        get(){
            val remoteView=RemoteViews(packageName, R.layout._notification).apply{
                setOnClickPendingIntent(R.id.play,buildIntent(PLAY_PAUSE))
                setOnClickPendingIntent(R.id.rr,buildIntent(RR))
                setOnClickPendingIntent(R.id.ff,buildIntent(FF))
                val buttonResource=if(episodeModel.isPlaying) R.drawable.icons8_pause else R.drawable.icons8_play
                setImageViewResource(R.id.play,buttonResource)
                setTextViewText(R.id.title,episodeModel.title)
                setTextViewText(R.id.author,episodeModel.author)
            }
            val notif= NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCustomContentView(remoteView)
                    .setDeleteIntent(buildIntent(DISMISS))
                    .setContentIntent(buildIntent(MEDIAPLAYER))
                    .build()

            val notificationTarget= NotificationTarget(this@MediaService,R.id.thumbnail,remoteView,notif, NOTIFICATION_ID)
            episodeModel.thumbnail?.let {
                remoteView.loadImage(this@MediaService,notificationTarget,it)
            }
            return notif
        }

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

        when(intent!!.action){
            PLAY_PAUSE-> episodeModel.play_pause()
            FF->{episodeModel.seekTo(episodeModel.position+30000)}
            RR->{episodeModel.seekTo(episodeModel.position-30000)}
            DISMISS -> stopSelf()
            MEDIAPLAYER->startActivity<MediaPlayerActivity>()
            STOP-> {
                episodeModel.stop()
                stopForeground(true)
                stopSelf()
            }
            else->  startForeground(NOTIFICATION_ID,notification)
        }
        return START_STICKY
    }


}

