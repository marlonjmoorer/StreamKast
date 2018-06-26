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
    private val playState:MutableLiveData<Int>
    private var timer:Timer?=null
    private  var controller: MutableLiveData<MediaControllerCompat>
    private  val position:MutableLiveData<Int>
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
        getApplication<Application>().unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        if (binder is MediaService.MediaBinder){
            binder.controller?.registerCallback(callback)
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
            //            toolbar.title=it.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
//            thumbnail_large.setImageBitmap(it.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
//            duration.text=it.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt().toTime()
//            seekBar.max=it.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
        }
    }


    private fun scheduleSeekbarUpdate() {
        stopSeekbarUpdate()
        timer= Timer()
        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {

            }
        },0,1000)
    }
    private fun stopSeekbarUpdate() {
        timer?.cancel()
    }




}