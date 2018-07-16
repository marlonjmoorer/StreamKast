package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import com.example.marlonmoorer.streamkast.MediaService
import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.models.EpisodeModel
import org.jetbrains.anko.doAsync
import java.util.*

class MediaPlayerViewModel:BaseViewModel(),ServiceConnection, View.OnClickListener {


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
                doAsync {
                    if(ep.duration>0){
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

    fun addToHistory(episode: EpisodeModel){
        doAsync {
            if(repository.history.exist(episode.guid)){
                return@doAsync
            }
            repository.history.insert(PlaybackHistory().apply {
                url=episode.url!!
                guid=episode.guid
                title=episode.title!!
                duration=episode.duration
                author=episode.author
                thumbnail=episode.thumbnail
                description=episode.description
            })
        }
    }
    fun restoreFromHistory()= repository.history.getMostRecent()


    val playbackState
        get() = playState?.value



}