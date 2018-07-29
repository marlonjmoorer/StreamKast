package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.databinding.FragmentMediaplayerBinding
import com.example.marlonmoorer.streamkast.models.EpisodeModel
import com.example.marlonmoorer.streamkast.models.MediaPlayerModel

import com.example.marlonmoorer.streamkast.ui.viewModels.MediaPlayerViewModel

import kotlinx.android.synthetic.main._now_playing.view.*
import kotlinx.android.synthetic.main.fragment_mediaplayer.*


class MediaPlayerFragment:Fragment(),SeekBar.OnSeekBarChangeListener{



    var mediaViewModel:MediaPlayerViewModel?=null
    var mediaPlayerModel= MediaPlayerModel()
    private lateinit var binding:FragmentMediaplayerBinding




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMediaplayerBinding.inflate(inflater).apply{
            nowPlaying?.run {
                title.isSelected=true
                playPause.setOnClickListener{
                    mediaViewModel?.onClick(it)
                }
            }
            playPause.setOnClickListener{
                mediaViewModel?.onClick(it)
            }
            seekbar.setOnSeekBarChangeListener(this@MediaPlayerFragment)
        }
        return  binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mediaViewModel=createViewModel()
        binding.model=mediaPlayerModel
        mediaViewModel?.isBound?.observe(this, Observer {bound->
            bound?.let {
                mediaViewModel?.run {
                    if(bound){
                        episode?.observe(this@MediaPlayerFragment,episodeObserver)
                        playState?.observe(this@MediaPlayerFragment,playStateObserver)
                        position?.observe(this@MediaPlayerFragment,positionObserver)
                    }else{
                        episode?.removeObserver(episodeObserver)
                        playState?.removeObserver(playStateObserver)
                        position?.removeObserver(positionObserver)
                    }
                    return@run
                }
            }
        })
    }


    override fun onProgressChanged(bar: SeekBar?, position:  Int, fromUser: Boolean) {
        mediaPlayerModel.Elapsed= position
    }

    override fun onStartTrackingTouch(bar: SeekBar) {
    }

    override fun onStopTrackingTouch(bar: SeekBar) {
        mediaViewModel?.seekTo(bar.progress.toLong())
    }

    val episodeObserver= Observer<EpisodeModel> { ep->
       ep?.let{
            mediaPlayerModel.run {
            Image=ep.thumbnail!!
            Title=ep.title!!
            Author= ep.author!!
            Duration=ep.duration?:0
         }
       }
    }
    val playStateObserver= Observer<Int> { state->
        when(state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                now_playing.play_pause.setImageResource(R.drawable.icons8_pause)
                play_pause.setImageResource(R.drawable.icons8_pause)
            }
            PlaybackStateCompat.STATE_BUFFERING-> {

            }

            PlaybackStateCompat.STATE_PAUSED,
            PlaybackStateCompat.STATE_NONE,
            PlaybackStateCompat.STATE_STOPPED-> {
                now_playing.play_pause.setImageResource(R.drawable.icons8_play)
                play_pause.setImageResource(R.drawable.icons8_play)
            }
        }
    }
    val positionObserver=Observer<Int>{ pos->
        pos?.let { mediaPlayerModel.Elapsed=pos}
    }


    fun fadeMiniPlayer(offset:Float)=now_playing?.fade(1 - (offset))

}