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
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import org.jetbrains.anko.doAsync

class MediaService:MediaBrowserServiceCompat(),AudioManager.OnAudioFocusChangeListener,MediaPlayer.OnPreparedListener  {

    private var mediaSession: MediaSessionCompat?=null
    private var mediaItem:MediaItem?=null
    private var mediaPlayer:MediaPlayer




    companion object {
        const val NOTIFICATION_ID=8888
        const val CHANNEL_ID="9999"
        const val MEDIAPLAYER="MEDIAPLAYER"
        const val MEDIA="MEDIA"
        const val PLAY_AUDIO="PLAY_AUDIO"
        val ACTION_PLAY = "ACTION_PLAY"
        val ACTION_PAUSE = "ACTION_PAUSE"
        val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        val ACTION_NEXT = "ACTION_NEXT"
        val ACTION_STOP = "ACTION_STOP"

    }

    init {
        mediaPlayer= MediaPlayer()
    }

    val transportControls
        get() = mediaSession?.controller?.transportControls

    override fun onCreate() {
        super.onCreate()
        val mediaButtonReciever=  ComponentName(applicationContext,MediaButtonReceiver::class.java)
        val intent= Intent(Intent.ACTION_MEDIA_BUTTON).apply {
            setClass(this@MediaService,MediaButtonReceiver::class.java)
        }
        val pendingIntent= PendingIntent.getBroadcast(this,0,intent,0)
        mediaSession = MediaSessionCompat(applicationContext, "Tag",mediaButtonReciever,null).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            setCallback(mediaSessionCallback)
            isActive=true
            setMediaButtonReceiver(pendingIntent)
        }

        registerReceiver(noisyReceiver,IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
        registerReceiver(playAudioReceiver, IntentFilter(PLAY_AUDIO))
    }

    fun handleIntent(intent: Intent){
        if(intent.hasExtra(MEDIA)){
            mediaItem = (intent.getSerializableExtra(MEDIA) as MediaItem).apply{
                this@MediaService.load(this.thumbnail,{bitmap-> this.bitmapImage = bitmap})
            }
            initMediaPlayer()
            updateMetaData()
            if(sessionToken==null)
                sessionToken=mediaSession?.sessionToken
        }
    }
    fun handleMediaButtonAction(intent: Intent){
        mediaSession?.controller?.transportControls?.run{
            when(intent.action){
                ACTION_PLAY-> play()
                ACTION_NEXT->skipToNext()
                ACTION_PREVIOUS->skipToPrevious()
                ACTION_PAUSE->pause()
                ACTION_STOP->stop()
            }
        }
    }

    private fun createAction(actionNumber: Int): PendingIntent {
        val playbackAction = Intent(this, MediaService::class.java)
         playbackAction.action =  when (actionNumber) {
            0 -> ACTION_PLAY
            1 -> ACTION_PAUSE
            2 -> ACTION_NEXT
            3 -> ACTION_PREVIOUS
            4-> ACTION_STOP
            else -> null
        }
        return PendingIntent.getService(this, actionNumber, playbackAction, 0)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handleIntent(intent)
        handleMediaButtonAction(intent)
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return START_STICKY
    }

    val notification:Notification
        get(){
            val play_pauseAction:PendingIntent
            val play_pause_icon:Int
            if(mediaPlayer.isPlaying) {
                play_pauseAction=createAction(1)
                play_pause_icon= R.drawable.icons8_pause
            }
            else {
                play_pauseAction=createAction(0)
                play_pause_icon= R.drawable.icons8_play
            }

            val mediaStyle=MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0,1,2)

            val activityIntent=intentFor<MainActivity>().apply {
                action= MEDIAPLAYER
            }
            val notif = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setShowWhen(false)
                    .setStyle(mediaStyle)
                    .setLargeIcon(mediaItem?.bitmapImage)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    .addAction(android.R.drawable.ic_media_previous, "previous",createAction(3) )
                    .addAction(play_pause_icon, "play",play_pauseAction)
                    .addAction(android.R.drawable.ic_media_next, "next",createAction(2))
                    .setContentText(mediaItem?.author)
                    .setContentTitle(mediaItem?.title)
                    .setDeleteIntent(createAction(4))
                    .setContentIntent(PendingIntent.getActivity(this,0,activityIntent,0))
                    .build()
            return notif
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


    private fun updateMetaData(){
        mediaSession?.setMetadata(MediaMetadataCompat.Builder()
            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,mediaItem?.bitmapImage)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaItem?.author)
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaItem?.title)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,mediaPlayer.duration.toLong())
            .build())
    }
    fun updateNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID,notification)
    }
    fun startForeground(){
        startForeground(NOTIFICATION_ID,notification)
    }

    override fun onPrepared(player: MediaPlayer) {
        transportControls?.play()
        updateMetaData()
        //startForeground()
    }

    private fun setPlaybackState(state: Int) {
        val action=if (state == PlaybackStateCompat.STATE_PLAYING)PlaybackStateCompat.ACTION_PAUSE
        else PlaybackStateCompat.ACTION_PLAY
        val playbackstateBuilder = PlaybackStateCompat.Builder()
                .setActions(action)
                .setState(state, mediaPlayer.currentPosition.toLong(),1.0f, SystemClock.elapsedRealtime())
        mediaSession?.setPlaybackState(playbackstateBuilder.build())
    }

    inner class MediaBinder: Binder() {
        val controller
            get() = mediaSession?.controller

    }
    data class MediaItem(
            var url:String?=null,
            var title:String?=null,
            var author:String?=null,
            var thumbnail:String?=null,
            var description: String?=null):Serializable{
        var bitmapImage:Bitmap?=null
    }

    private val noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (mediaPlayer.isPlaying==true) {
                transportControls?.pause()
            }
        }
    }
    private val playAudioReceiver = object :BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            handleIntent(intent)
        }

    }
    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            super.onPlay()
            if(successfullyRetrievedAudioFocus()){
                mediaPlayer.start()
                mediaSession?.setActive(true)
                setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                startForeground()
            }
        }

        override fun onPause() {
            super.onPause()
            mediaPlayer.pause()
            setPlaybackState(PlaybackStateCompat.STATE_PAUSED)
            stopForeground(false)
            updateNotification()

        }

        override fun onStop() {

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
                    transportControls?.stop()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                transportControls?.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer.setVolume(0.3f, 0.3f)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (!mediaPlayer.isPlaying()) {
                    transportControls?.play()
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
        result.sendResult(null)
    }





}

