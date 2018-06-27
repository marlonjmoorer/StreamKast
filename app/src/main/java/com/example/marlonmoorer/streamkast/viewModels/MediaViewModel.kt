package com.example.marlonmoorer.streamkast.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.MediaService
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.toTime
import kotlinx.android.synthetic.main.activity_media_player.*
import java.util.*

class MediaViewModel(application: Application):AndroidViewModel(application),ServiceConnection,LifecycleObserver {


    private var previousState:PlaybackStateCompat?=null
    val playState:MutableLiveData<Int>
    private var timer:Timer?=null
    var controller: MutableLiveData<MediaControllerCompat>
    val position:MutableLiveData<Int>
    val metadata:MutableLiveData<MediaMetadataCompat>

    val application
            get()=getApplication<App>()
    init {
        playState= MutableLiveData()
        position=MutableLiveData()
        controller= MutableLiveData()
        metadata= MutableLiveData()
    }

    fun connect(){
        val intent= Intent(application,MediaService::class.java)
        application.bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
    }
    fun disconnect() {
        application.unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        if (binder is MediaService.MediaBinder){
            with(binder.controller!!){
                registerCallback(callback)
                val state=playbackState
                updatePlaybackState(state)
                updateMetadata(metadata)
                when(state?.state){
                    PlaybackStateCompat.STATE_PLAYING,PlaybackStateCompat.STATE_BUFFERING -> scheduleSeekbarUpdate()
                }
            }
            controller.postValue(binder.controller)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        controller.value?.unregisterCallback(callback)
        controller.postValue(null)
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

    override fun onCleared() {
        super.onCleared()
        application.unbindService(this)
    }



}