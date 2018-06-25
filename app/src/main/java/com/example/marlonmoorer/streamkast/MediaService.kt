package com.example.marlonmoorer.streamkast

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import java.net.URL
import android.support.v4.media.session.MediaSessionCompat
import android.content.BroadcastReceiver
import android.media.AudioManager
import android.content.IntentFilter
import android.content.ComponentName
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaButtonReceiver
import android.text.TextUtils
import org.jetbrains.anko.intentFor
import java.io.Serializable
import java.util.*

class MediaService:MediaBrowserServiceCompat(),AudioManager.OnAudioFocusChangeListener,MediaPlayer.OnPreparedListener  {

    private var mediaSession: MediaSessionCompat? = null
    private var mediaItem:MediaItem?=null
    private var mediaPlayer:MediaPlayer
    private var mediaModel:MediaModel?=null
    private  val timer:Timer

    companion object {
        const val NOTIFICATION_ID=8888
        const val CHANNEL_ID="9999"
        const val MEDIAPLAYER="MEDIAPLAYER"
        const val MEDIA="MEDIA"
        const val PLAY_AUDIO="PLAY_AUDIO"
    }

    init {
        mediaPlayer= MediaPlayer()
        timer= Timer()
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(noisyReceiver,IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
        registerReceiver(playAudioReceiver, IntentFilter(PLAY_AUDIO))
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(intent.hasExtra(MEDIA)){
            mediaItem= intent.getSerializableExtra(MEDIA) as MediaItem
            initMediaPlayer()
            initMediaSession()
            updateMetaData()
            sessionToken=mediaSession?.sessionToken
        }
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return START_STICKY
    }

    val notification:Notification
        get(){
            val play_pauseAction= if(mediaPlayer.isPlaying) playbackAction(PlaybackStateCompat.ACTION_PAUSE)
            else playbackAction(PlaybackStateCompat.ACTION_PLAY)

           // val largeIcon = BitmapFactory.decodeResource(resources,R.drawable.icons8_technology)
                    //BitmapFactory.decodeStream(URL(mediaItem?.thumbnail).openStream())

            val mediaStyle=MediaStyle()
                    .setMediaSession(mediaSession!!.sessionToken)
                    .setShowActionsInCompactView(0,1,2)

            val play_pause_icon= if(mediaPlayer.isPlaying) R.drawable.icons8_pause else R.drawable.icons8_play

            val notif = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setShowWhen(false)
                    .setStyle(mediaStyle)
                  //  .setLargeIcon(largeIcon)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    .addAction(android.R.drawable.ic_media_previous, "previous",playbackAction(PlaybackStateCompat.ACTION_REWIND) )
                    .addAction(play_pause_icon, "play",play_pauseAction)
                    .addAction(android.R.drawable.ic_media_next, "next",playbackAction(PlaybackStateCompat.ACTION_FAST_FORWARD))
                    .setContentText(mediaItem?.author)
                    .setContentTitle(mediaItem?.title)
                    .setDeleteIntent(playbackAction(PlaybackStateCompat.ACTION_STOP))
                    .setContentIntent(PendingIntent.getActivity(this,0,intentFor<MediaPlayerActivity>(),0))
                    .build()
            return notif
        }

    private fun playbackAction(action:Long): PendingIntent? {
       return MediaButtonReceiver.buildMediaButtonPendingIntent(this,action)
    }



    override fun onBind(intent: Intent?): IBinder? {
        return MediaBinder()
    }

    private fun initMediaPlayer(){
        with(mediaPlayer){
            reset()
            setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setVolume(1.0f, 1.0f)
            setDataSource(mediaItem?.url)
            setOnPreparedListener(this@MediaService)
            prepareAsync()
        }
    }
    private fun initMediaSession() {
        val mediaButtonReciever=  ComponentName(applicationContext,MediaButtonReceiver::class.java)
        val intent= Intent(Intent.ACTION_MEDIA_BUTTON).apply {
            setClass(this@MediaService,MediaButtonReceiver::class.java)
        }
        val pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        mediaSession = MediaSessionCompat(applicationContext, "Tag",mediaButtonReciever,null).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            setCallback(mediaSessionCallback)
            isActive=true
            mediaModel= MediaModel(controller)
            setMediaButtonReceiver(pendingIntent)
        }
    }

    private fun updateMetaData()= async {
        val albumArt = BitmapFactory.decodeResource(resources,R.drawable.icons8_technology) //BitmapFactory.decodeStream(URL(mediaItem?.thumbnail).openStream())
        mediaSession!!.setMetadata(MediaMetadataCompat.Builder()
            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaItem?.author)
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaItem?.title)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,mediaPlayer.duration.toLong())
            .build())
    }
    fun updateNotification()= async{
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID,notification)
    }
    fun startForeground()= async {
        startForeground(NOTIFICATION_ID,notification)
    }

    override fun onPrepared(player: MediaPlayer) {
        mediaSession?.controller?.transportControls?.play()
        updateMetaData()
        startForeground()
        timer.scheduleAtFixedRate(object :TimerTask(){
            override fun run() {
                if(mediaPlayer.isPlaying){
                    setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                }
            }
        },0,1000)
    }

    private fun setPlaybackState(state: Int) {
        val action=if (state == PlaybackStateCompat.STATE_PLAYING)PlaybackStateCompat.ACTION_PAUSE
        else PlaybackStateCompat.ACTION_PLAY
        val playbackstateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or action)
                .setState(state, mediaPlayer.currentPosition.toLong(), 0f)
        mediaSession?.setPlaybackState(playbackstateBuilder.build())
    }

    inner class MediaBinder: Binder() {
        fun getMediaModel()=mediaModel
    }
    data class MediaItem(
            var url:String?=null,
            var title:String?=null,
            var author:String?=null,
            var thumbnail:String?=null,
            var description: String?=null):Serializable

    private val noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (mediaModel?.playing==true) {
                mediaModel?.pause()
            }
        }
    }
    private val playAudioReceiver = object :BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {

            if(intent.hasExtra(MEDIA)){
                mediaItem= intent.getSerializableExtra(MEDIA) as MediaItem
                initMediaPlayer()
                initMediaSession()
                updateMetaData()
            }

        }

    }
    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            super.onPlay()
            if(successfullyRetrievedAudioFocus()){
                mediaSession?.setActive(true)
                setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                startForeground()
                mediaPlayer.start()
                mediaModel?.notifyChange()

            }
        }

        override fun onPause() {
            super.onPause()
            setPlaybackState(PlaybackStateCompat.STATE_PAUSED)
            stopForeground(false)
            mediaPlayer.pause()
            updateNotification()
            mediaModel?.notifyChange()

        }

        override fun onStop() {
            timer.cancel()
            mediaPlayer.reset()
            mediaPlayer.release()
            stopForeground(true)
            stopSelf()

        }

        override fun onSeekTo(pos: Long) {
            mediaPlayer.seekTo(pos.toInt())
        }

    }
    override fun onAudioFocusChange(action: Int) {
        when (action) {
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
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start()
                }
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
        }
    }

    private fun successfullyRetrievedAudioFocus(): Boolean {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        return result == AudioManager.AUDIOFOCUS_GAIN
    }


    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        if(TextUtils.equals(clientPackageName, packageName)) {
            return BrowserRoot(getString(R.string.app_name), null)
        }
        return null;
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(null);
    }





}

