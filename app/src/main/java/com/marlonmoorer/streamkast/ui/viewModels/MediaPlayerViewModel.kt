package com.marlonmoorer.streamkast.ui.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import com.marlonmoorer.streamkast.MediaService
import com.marlonmoorer.streamkast.Utils
import com.marlonmoorer.streamkast.data.PlaybackHistory
import com.marlonmoorer.streamkast.models.EpisodeModel
import com.marlonmoorer.streamkast.models.IEpisode
import org.jetbrains.anko.doAsync
import java.util.*

class MediaPlayerViewModel:BaseViewModel(),ServiceConnection {


    var episode:LiveData<EpisodeModel>?=null
    var playState:LiveData<Int>?=null
    var isBound:MutableLiveData<Boolean>?=null
    var position:LiveData<Int>?=null
    var controls:MediaControllerCompat.TransportControls?=null

    init {
        isBound= MutableLiveData()
    }



    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        if (binder is MediaService.MediaBinder){
            episode=Transformations.map(binder.episdodeData,{ep->
                doAsync(Utils.exceptionHandler){
                    if(ep.duration!=0){

                        addToHistory(ep)
                    }
                }
                return@map ep
            })
            playState= binder.playbackState
            controls=binder.controller?.transportControls
            position=binder.currentPosition
            isBound?.postValue(true)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isBound?.postValue(false)
    }



    fun togglePlayback() {
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
    fun fastForward()=controls?.fastForward()
    fun rewind()=controls?.rewind()

    fun addToHistory(episode: EpisodeModel){
        doAsync(Utils.exceptionHandler) {
            if(repository.history.exist(episode.guid)){
                repository.history.uppdateLastPlayed(Date(),episode.guid)
                return@doAsync
            }
            repository.history.insert(IEpisode.fromEpisode<PlaybackHistory>(episode).apply {
                lastPlayed=Date()
            })
        }
    }
    fun restoreFromHistory()= repository.history.getMostRecent()


    val playbackState
        get() = playState?.value



}