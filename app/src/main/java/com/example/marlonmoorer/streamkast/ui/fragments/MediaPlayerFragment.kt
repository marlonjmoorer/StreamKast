package com.example.marlonmoorer.streamkast.ui.fragments

import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
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
                    mediaViewModel?.togglePlayback()
                }
            }
            playPause.setOnClickListener{
                mediaViewModel?.togglePlayback()
            }
            fastForward.setOnClickListener{
                mediaViewModel?.fastForward()
            }
            rewind.setOnClickListener{
                mediaViewModel?.rewind()
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
            Image=ep.thumbnail
            Title=ep.title
            Author= ep.author
            Duration=ep.duration?:0
         }
       }
    }
    val playStateObserver= Observer<Int> { state->
        mediaPlayerModel.playbackState=state?:0
    }
    val positionObserver=Observer<Int>{ position->
        position?.let { mediaPlayerModel.Elapsed=position}
    }

    fun setOffset(offset:Float)=now_playing?.fade(1 - (offset))

    companion object {
        private val playImage=R.drawable.ic_play_arrow_solid
        private val playImageSmall=R.drawable.ic_play_arrow_solid_small
        private val pauseImage=R.drawable.ic_pause_solid
        private val pauseImageSmall=R.drawable.ic_pause_solid_small


    }

}