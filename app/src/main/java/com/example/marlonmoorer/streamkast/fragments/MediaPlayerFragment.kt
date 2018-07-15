package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.databinding.FragmentMediaplayerBinding
import com.example.marlonmoorer.streamkast.models.EpisodeModel
import com.example.marlonmoorer.streamkast.models.MediaPlayerModel

import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.MediaViewModel

import kotlinx.android.synthetic.main._now_playing.view.*
import kotlinx.android.synthetic.main.fragment_mediaplayer.*


class MediaPlayerFragment:Fragment(),SeekBar.OnSeekBarChangeListener{


    private var detailViewModel: DetailViewModel?=null
    var mediaViewModel:MediaViewModel?=null
    var mediaPlayerModel= MediaPlayerModel()
    private var binding:FragmentMediaplayerBinding?=null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMediaplayerBinding.inflate(inflater).apply{
            model=mediaPlayerModel
            nowPlaying?.run {
                title.isSelected=true
                playPause.setOnClickListener(mediaViewModel)
            }
            playPause.setOnClickListener(mediaViewModel)
            seekbar.setOnSeekBarChangeListener(this@MediaPlayerFragment)
        }
//        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
//            episode?.let {
//                val media=EpisodeModel(
//                        title=episode.title,
//                        author=episode.author,
//                        thumbnail=episode.thumbnail,
//                        url=episode.mediaUrl,
//                        description=episode.description
//                )
//                mediaViewModel?.setMedia(media)
//            }
//        })
        mediaViewModel?.run{
            metadata.observe(this@MediaPlayerFragment, Observer { data->
                data?.let {
                    mediaPlayerModel.Image=data.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
                    mediaPlayerModel.Title=data.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
                    mediaPlayerModel.Duration=it.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
                }
            })
            playState.observe(this@MediaPlayerFragment, Observer { state->
                when(state) {
                    PlaybackStateCompat.STATE_PLAYING -> {
                        now_playing.play_pause.setImageResource(R.drawable.icons8_pause)
                        play_pause.setImageResource(R.drawable.icons8_pause)
                    }
                    PlaybackStateCompat.STATE_PAUSED,
                    PlaybackStateCompat.STATE_NONE,
                    PlaybackStateCompat.STATE_STOPPED,
                    PlaybackStateCompat.STATE_BUFFERING-> {
                        now_playing.play_pause.setImageResource(R.drawable.icons8_play)
                        play_pause.setImageResource(R.drawable.icons8_play)
                    }
                }
            })
            position.observe(this@MediaPlayerFragment, Observer{ pos->
                pos?.let { mediaPlayerModel.Elapsed=pos}
            })
        }
        return  binding?.root
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailViewModel=createViewModel()
        mediaViewModel=createViewModel()
    }


    override fun onProgressChanged(bar: SeekBar?, position:  Int, fromUser: Boolean) {
        mediaPlayerModel.Elapsed= position
    }

    override fun onStartTrackingTouch(bar: SeekBar) {
       // mediaViewModel?.pause()
    }

    override fun onStopTrackingTouch(bar: SeekBar) {
        mediaViewModel?.seekTo(bar.progress.toLong())
        //mediaViewModel?.play()
    }

    fun fadeMiniPlayer(offset:Float)=now_playing?.fade(1 - (offset))



}