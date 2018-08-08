package com.marlonmoorer.streamkast

import android.app.Notification
import android.app.NotificationChannel
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
import android.media.session.PlaybackState
import android.net.Uri
import android.os.*
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaButtonReceiver
import android.text.TextUtils
import org.jetbrains.anko.intentFor
import android.webkit.URLUtil
import com.marlonmoorer.streamkast.ui.activities.MainActivity
import com.marlonmoorer.streamkast.listeners.IMediaListener
import com.marlonmoorer.streamkast.models.EpisodeModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class MediaService:MediaBrowserServiceCompat(),AudioManager.OnAudioFocusChangeListener {

    private var mediaSession: MediaSessionCompat?=null
    private lateinit var exoPlayer: SimpleExoPlayer
    private val BANDWIDTH_METER = DefaultBandwidthMeter()
    //private lateinit var mediaPlayer:MediaPlayer
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
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(BANDWIDTH_METER), DefaultLoadControl());
        episodeData=MutableLiveData()
        playbackStateData=MutableLiveData()
        position= MutableLiveData()
        timer=Timer()
        registerReceiver(noisyReceiver,IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
        registerReceiver(playAudioReceiver, IntentFilter(PLAY_AUDIO))

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


             val channel =  NotificationChannel(CHANNEL_ID,application.packageName,NotificationManager.IMPORTANCE_LOW)
             val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)

           // mNotificationManager.createNotificationChannel(channel);
        }


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
    private  val isPlaying
            get() = exoPlayer.run {
               playWhenReady  && playbackState==Player.STATE_READY
            }

    val notification:Notification
        get(){
            val play_pauseAction:PendingIntent
            val play_pause_icon:Int
            if(isPlaying) {
                play_pauseAction=createAction(1)
                play_pause_icon= R.drawable.ic_pause_solid
            }
            else {
                play_pauseAction=createAction(0)
                play_pause_icon= R.drawable.ic_play_arrow_solid
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
                    .addAction(R.drawable.ic_replay_30, getString(R.string.previous_text),createAction(3) )
                    .addAction(play_pause_icon, getString(R.string.play_text),play_pauseAction)
                    .addAction(R.drawable.ic_forward_30, getString(R.string.next_text),createAction(2))
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


    private  fun buildMediaSource(url:String):ExtractorMediaSource{

        val uri= Uri.parse(url)
        if(URLUtil.isHttpUrl(url)||URLUtil.isHttpsUrl(url)){

           return ExtractorMediaSource.Factory(
                    DefaultHttpDataSourceFactory(getString(R.string.user_agent_text))).createMediaSource(uri)
        }else {
            val dataSpec = DataSpec(uri)
            val fileDataSource = FileDataSource()
            val factory = object : DataSource.Factory {
                override fun createDataSource(): DataSource {
                    return fileDataSource
                }
            }
            fileDataSource.open(dataSpec)
            return ExtractorMediaSource.Factory(factory).createMediaSource(uri)
        }

    }


    private fun prepareMediaPlayer(media:EpisodeModel) {

        try {

            val mediaSource = buildMediaSource(media.url)
            exoPlayer.prepare(mediaSource, true, false)
            episodeData.postValue(media)
            if(media.autoPlay){
                transportControls?.play()
            }else{
                playbackStateData.postValue(PlaybackStateCompat.STATE_PAUSED)
            }

            exoPlayer.addListener(object :IMediaListener{

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when(playbackState){
                        Player.STATE_BUFFERING->{
                            setPlaybackState(PlaybackState.STATE_BUFFERING)
                        }
                        Player.STATE_READY->{
                            val state=if(playWhenReady)PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
                            setPlaybackState(state)
                        }
                    }
                }

            })
        }catch (ex:Exception){
            stopSelf()
        }


    }


    private fun setPlaybackState(state: Int) {
        val action=if (state == PlaybackStateCompat.STATE_PLAYING)
            PlaybackStateCompat.ACTION_PAUSE
        else
            PlaybackStateCompat.ACTION_PLAY
        val playbackstateBuilder = PlaybackStateCompat.Builder()
                .setActions(action)
                .setState(state,exoPlayer.contentPosition,1.0f, SystemClock.elapsedRealtime())
        mediaSession?.setPlaybackState(playbackstateBuilder.build())
        playbackStateData.postValue(state)
    }

    fun updateNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID,notification)
    }
    fun startForeground(){
        startForeground(NOTIFICATION_ID,notification)
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
            if (isPlaying) {
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
                exoPlayer.playWhenReady=true
                mediaSession?.setActive(true)
                setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                startSeekbarUpdate()
                startForeground()
            }
        }

        override fun onPause() {
            super.onPause()
            exoPlayer.playWhenReady=false
            setPlaybackState(PlaybackStateCompat.STATE_PAUSED)
            stopForeground(false)
            stopSeekbarUpdate()
            updateNotification()
        }

        override fun onStop() {

            exoPlayer.stop()
            exoPlayer.release()
            stopForeground(true)
            stopSelf()

        }

        override fun onSeekTo(pos: Long) {
            stopSeekbarUpdate()
            exoPlayer.seekTo(pos)
            startSeekbarUpdate()

        }
        override fun onRewind() {
            exoPlayer.seekTo(exoPlayer.currentPosition-30000)
        }
        override fun onFastForward() {
            exoPlayer.seekTo(exoPlayer.currentPosition+30000)
        }
    }
    override fun onAudioFocusChange(action: Int) {
        when (action) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (isPlaying) {
                    transportControls?.pause()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                transportControls?.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                exoPlayer.setVolume(0.3f)

            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (isPlaying) {
                    transportControls?.play()
                }
                exoPlayer.setVolume(1.0f)
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
             position.postValue(exoPlayer.currentPosition.toInt())
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

