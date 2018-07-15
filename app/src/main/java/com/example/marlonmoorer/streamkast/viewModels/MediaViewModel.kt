package com.example.marlonmoorer.streamkast.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.MediaService
import com.example.marlonmoorer.streamkast.models.EpisodeModel
import java.util.*

class MediaViewModel:BaseViewModel(),ServiceConnection, View.OnClickListener {



    private var previousState:PlaybackStateCompat?=null
    private var timer:Timer?=null
    
    val playState:MutableLiveData<Int>
    val position:MutableLiveData<Int>
    val metadata:MutableLiveData<MediaMetadataCompat>
    var bound:MutableLiveData<Boolean>
    var controls:MediaControllerCompat.TransportControls?=null



    init {
        playState= MutableLiveData()
        position=MutableLiveData()
        metadata= MutableLiveData()
        bound=MutableLiveData()
        bound.value=false
    }



    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        if (binder is MediaService.MediaBinder){
            bound.postValue(true)
            binder.controller?.run {
                registerCallback(callback)
                val state=playbackState
                updatePlaybackState(state)
                updateMetadata(metadata)
                when(state?.state){
                    PlaybackStateCompat.STATE_PLAYING,PlaybackStateCompat.STATE_BUFFERING -> scheduleSeekbarUpdate()
                }
                controls=transportControls
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        bound.postValue(false)
    }


    private val callback=object : MediaControllerCompat.Callback(){
        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            this@MediaViewModel.metadata.postValue(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) =updatePlaybackState(state);
    }

    private fun updatePlaybackState(state: PlaybackStateCompat?) {
        state?.let {
            previousState=state
            when (state.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    scheduleSeekbarUpdate()
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    stopSeekbarUpdate()
                }
                PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> {
                    stopSeekbarUpdate()
                }
                PlaybackStateCompat.STATE_BUFFERING -> {
                    stopSeekbarUpdate()
                }
            }
            playState.postValue(it.state)
        }
    }
    private  fun updateMetadata(metadata:MediaMetadataCompat?){
        metadata?.let {
            this.metadata.postValue(it)
        }
    }


    private fun scheduleSeekbarUpdate() {
        stopSeekbarUpdate()
        timer= Timer()
        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                updateProgress()
            }
        },0,1000)
    }
    private fun stopSeekbarUpdate() {
        timer?.cancel()
    }
    private fun updateProgress() {
        previousState?.let {
            var currentPosition = it.getPosition()
            if (it.getState() == PlaybackStateCompat.STATE_PLAYING) {
                val timeDelta = SystemClock.elapsedRealtime() - it.lastPositionUpdateTime
                currentPosition += timeDelta.toInt() * it.playbackSpeed.toLong()
            }
            position.postValue(currentPosition.toInt())
        }
    }



    override fun onClick(view :View?) {
        playbackState?.let {
            when (it) {
                PlaybackStateCompat.STATE_PLAYING
                    , PlaybackStateCompat.STATE_BUFFERING -> {
                    pause()
                }
                PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {
                    play()
                }
                else->{}
            }
        }
    }

    fun play()=controls?.play()
    fun pause()=controls?.pause()
    fun seekTo(position: Long)=controls?.seekTo(position)
    val playbackState
        get() = playState.value



}