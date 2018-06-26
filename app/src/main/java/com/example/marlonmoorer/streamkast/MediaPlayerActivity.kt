package com.example.marlonmoorer.streamkast

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_media_player.*
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import android.widget.SeekBar

import android.R.string.cancel
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import java.util.*
import android.support.v4.media.MediaMetadataCompat
import android.os.SystemClock








class MediaPlayerActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {


    private  var previousState:PlaybackStateCompat?=null
    //private lateinit var binding: ActivityMediaPlayerBinding
    private var timer:Timer?=null
    private  var controller: MediaControllerCompat?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_media_player)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }
        val intent= Intent(this,MediaService::class.java)
        bindService(intent,serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)

    }
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                controller=binder.controller
                controller?.registerCallback(callback)
                val state=controller?.playbackState
                updatePlaybackState(state)
                updateMetadata(controller?.metadata)
                updateProgress()
                when(state?.state){
                    PlaybackStateCompat.STATE_PLAYING,PlaybackStateCompat.STATE_BUFFERING -> scheduleSeekbarUpdate()
                }

                seekBar.setOnSeekBarChangeListener(this@MediaPlayerActivity)
                play.setOnClickListener {
                    controller?.playbackState?.let {
                        val controls = controller?.transportControls
                        when (it.state) {
                            PlaybackStateCompat.STATE_PLAYING
                                , PlaybackStateCompat.STATE_BUFFERING -> {
                                controls?.pause()
                                stopSeekbarUpdate()
                            }
                            PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {
                                controls?.play()
                                scheduleSeekbarUpdate()
                            }
                        }
                    }
                }

                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
            }
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            controller?.unregisterCallback(callback)
            controller=null

        }
    }



    override fun onProgressChanged(bar: SeekBar?, position:  Int, fromUser: Boolean) {
        elapsed.text= position.toTime()

    }

    override fun onStartTrackingTouch(bar: SeekBar) {
        stopSeekbarUpdate()
        controller?.transportControls?.pause()
    }

    override fun onStopTrackingTouch(bar: SeekBar) {
        scheduleSeekbarUpdate()
        controller?.transportControls?.seekTo(bar.progress.toLong())
        controller?.transportControls?.play()
        updateProgress()

    }




    private fun scheduleSeekbarUpdate() {
        stopSeekbarUpdate()
        timer= Timer()
        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                runOnUiThread {
                    updateProgress()
                }
            }
        },0,1000)
    }

    private fun stopSeekbarUpdate() {
           timer?.cancel()
    }

    private fun updateProgress() {
        previousState?.let {
            var currentPosition = it.getPosition()
            if (it.getState() === PlaybackStateCompat.STATE_PLAYING) {
                // Calculate the elapsed time between the last position update and now and unless
                // paused, we can assume (delta * speed) + current position is approximately the
                // latest position. This ensure that we do not repeatedly call the getPlaybackState()
                // on MediaControllerCompat.
                val timeDelta = SystemClock.elapsedRealtime() - it.lastPositionUpdateTime
                currentPosition += timeDelta.toInt() * it.playbackSpeed.toLong()
               // currentPosition=controller?.playbackState?.position?:0
            }
            seekBar.setProgress(currentPosition.toInt())
        }
    }

    private val callback=object :MediaControllerCompat.Callback(){
        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            updateMetadata(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) =updatePlaybackState(state);
    }

    private fun updatePlaybackState(state: PlaybackStateCompat?) {
        state?.let {
        previousState= state

            when (state.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    play.setBackgroundResource(R.drawable.icons8_pause)
                    scheduleSeekbarUpdate()
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    play.setBackgroundResource(R.drawable.icons8_play)
                    stopSeekbarUpdate()
                }
                PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> {

                    play.setBackgroundResource(R.drawable.icons8_play)
                    stopSeekbarUpdate()
                }
                PlaybackStateCompat.STATE_BUFFERING -> {
                    stopSeekbarUpdate()
                }

            }
        }
    }

    private  fun updateMetadata(metadata:MediaMetadataCompat?){
        metadata?.let {
            toolbar.title=it.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            thumbnail_large.setImageBitmap(it.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
            duration.text=it.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt().toTime()
            seekBar.max=it.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
        }
    }




}
