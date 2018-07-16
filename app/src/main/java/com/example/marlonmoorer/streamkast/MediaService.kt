package com.example.marlonmoorer.streamkast

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.media.session.MediaSessionCompat
import android.content.BroadcastReceiver
import android.media.AudioManager
import android.content.IntentFilter
import android.content.ComponentName
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.os.*
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaButtonReceiver
import android.text.TextUtils
import org.jetbrains.anko.intentFor
import android.util.Log
import com.example.marlonmoorer.streamkast.models.EpisodeModel
import org.jetbrains.anko.runOnUiThread
import java.util.*

class MediaService:MediaBrowserServiceCompat(),AudioManager.OnAudioFocusChangeListener,MediaPlayer.OnBufferingUpdateListener  {

    private var mediaSession: MediaSessionCompat?=null

    private lateinit var mediaPlayer:MediaPlayer
    private lateinit var episodeData:MutableLiveData<EpisodeModel>
    private lateinit var playbackStateData: MutableLiveData<Int>
    private lateinit var position:MutableLiveData<Int>
    private var bitmapImage: Bitmap?=null
    private lateinit var timer: Timer

    companion object {
        val NOTIFICATION_ID=8888
        val CHANNEL_ID="9999"
        val MEDIAPLAYER="MEDIAPLAYER"
        val MEDIA="MEDIA"
        val PLAY_AUDIO="PLAY_AUDIO"
        val ACTION_PLAY = "ACTION_PLAY"
        val ACTION_PAUSE = "ACTION_PAUSE"
        val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        val ACTION_NEXT = "ACTION_NEXT"
        val ACTION_STOP = "ACTION_STOP"
        val ACTION_FF = "ACTION_FF"
        val ACTION_RR = "ACTION_RR"
    }


    val currentMedia
        get()= episodeData.value

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
        mediaPlayer= MediaPlayer()
        episodeData=MutableLiveData()
        playbackStateData=MutableLiveData()
        position= MutableLiveData()
        timer=Timer()
        registerReceiver(noisyReceiver,IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
        registerReceiver(playAudioReceiver, IntentFilter(PLAY_AUDIO))
    }

    override fun onDestroy() {
        unregisterReceiver(noisyReceiver)
        unregisterReceiver(playAudioReceiver)
    }

    fun handleIntent(intent: Intent){
        if(intent.hasExtra(MEDIA)){
            val media= (intent.getSerializableExtra(MEDIA) as EpisodeModel)
            applicationContext.loadAsBitmap(media.thumbnail){
                bitmap-> bitmapImage = bitmap
            }
            prepareMediaPlayer(media)
            if(sessionToken==null)
                sessionToken=mediaSession?.sessionToken
        }
    }
    fun handleMediaButtonAction(intent: Intent){
       transportControls?.run{
            when(intent.action){
                ACTION_PLAY-> play()
                ACTION_NEXT->skipToNext()
                ACTION_PREVIOUS->skipToPrevious()
                ACTION_PAUSE->pause()
                ACTION_STOP->stop()
                ACTION_RR-> rewind()
                ACTION_FF->fastForward()
            }
        }
    }

    private fun createAction(actionNumber: Int): PendingIntent {
        val playbackAction = Intent(this, MediaService::class.java)
         playbackAction.action =  when (actionNumber) {
            0 -> ACTION_PLAY
            1 -> ACTION_PAUSE
            2 -> ACTION_FF
            3 -> ACTION_RR
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
           return  NotificationCompat.Builder(this, CHANNEL_ID)
                    .setShowWhen(false)
                    .setStyle(mediaStyle)
                    .setLargeIcon(bitmapImage)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    .addAction(android.R.drawable.ic_media_previous, "previous",createAction(3) )
                    .addAction(play_pause_icon, "play",play_pauseAction)
                    .addAction(android.R.drawable.ic_media_next, "next",createAction(2))
                    .setContentText(currentMedia?.author)
                    .setContentTitle(currentMedia?.title)
                    .setDeleteIntent(createAction(4))
                    .setContentIntent(PendingIntent.getActivity(this,0,activityIntent,0))
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .build()
        }


    override fun onBind(intent: Intent?): IBinder? {
        return MediaBinder()
    }

    private fun prepareMediaPlayer(media:EpisodeModel) {
        with(mediaPlayer){
            reset()
            setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setVolume(1.0f, 1.0f)
            setDataSource(media.url)
            setOnPreparedListener{
                media.duration=it.duration
                episodeData.postValue(media)
                transportControls?.play()
            }
            setOnBufferingUpdateListener(this@MediaService)
            setOnErrorListener{mp,what, extra->
                when(what){
                    MediaPlayer.MEDIA_ERROR_TIMED_OUT->
                            transportControls?.stop()
                    MediaPlayer.MEDIA_ERROR_IO ->
                            transportControls?.pause()
                }
                return@setOnErrorListener true
            }
            setOnInfoListener{mp,what, extra->
                when(what){
                    MediaPlayer.MEDIA_INFO_BUFFERING_START->  stopSeekbarUpdate()
                    MediaPlayer.MEDIA_INFO_BUFFERING_END-> startSeekbarUpdate()
                    else-> return@setOnInfoListener false
                }
                return@setOnInfoListener  true
            }
            setOnSeekCompleteListener {mp->

            }
            setOnCompletionListener {
                val state= mediaSession?.controller?.playbackState?.state
                Log.w("info","state: $state")
                transportControls?.stop()
            }
            prepareAsync()
        }
    }



    fun updateNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID,notification)
    }
    fun startForeground(){
        startForeground(NOTIFICATION_ID,notification)
    }


    override fun onBufferingUpdate(plater: MediaPlayer?, percent: Int) {
        Log.w("Buffer","$percent %")
    }


    inner class MediaBinder: Binder() {
        val controller
            get() = mediaSession?.controller
        val episdodeData:LiveData<EpisodeModel>
            get() = this@MediaService.episodeData
        val playbackState:LiveData<Int>
            get() =  this@MediaService.playbackStateData
        val currentPosition:LiveData<Int>
            get() =  this@MediaService.position


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
                playbackStateData.postValue(PlaybackStateCompat.STATE_PLAYING)
                startSeekbarUpdate()
                startForeground()
            }
        }

        override fun onPause() {
            super.onPause()
            mediaPlayer.pause()
            playbackStateData.postValue(PlaybackStateCompat.STATE_PAUSED)
            stopForeground(false)
            stopSeekbarUpdate()
            updateNotification()
        }

        override fun onStop() {

            mediaPlayer.reset()
            mediaPlayer.release()
            stopForeground(true)
            stopSelf()

        }

        override fun onSeekTo(pos: Long) {
            stopSeekbarUpdate()
            mediaPlayer.seekTo(pos.toInt())
            startSeekbarUpdate()

        }

        override fun onRewind() {
            mediaPlayer.seekTo(mediaPlayer.currentPosition-30000)
        }

        override fun onFastForward() {
            mediaPlayer.seekTo(mediaPlayer.currentPosition+30000)
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

     private fun updateProgress(){
         if(playbackStateData.value==PlaybackState.STATE_PLAYING){
             position.postValue(mediaPlayer.currentPosition)
         }
     }


    private fun startSeekbarUpdate() {
        stopSeekbarUpdate()
        timer= Timer()
        timer.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                updateProgress()
            }
        },0,1000)
    }
    private fun stopSeekbarUpdate() {
        timer.cancel()
    }

}

