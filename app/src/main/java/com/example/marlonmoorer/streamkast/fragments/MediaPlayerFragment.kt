package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.databinding.FragmentMediaplayerBinding

import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.MediaViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main._now_playing.view.*
import kotlinx.android.synthetic.main.fragment_mediaplayer.*
import org.jetbrains.anko.support.v4.startService


class MediaPlayerFragment:Fragment(),SeekBar.OnSeekBarChangeListener, View.OnClickListener,SlidingUpPanelLayout.PanelSlideListener{


    private var detailViewModel: DetailViewModel?=null
    var mediaViewModel:MediaViewModel?=null
    var mediaPlayerModel=MediaPlayerModel()
    private  var controller: MediaControllerCompat?=null
    private var binding:FragmentMediaplayerBinding?=null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMediaplayerBinding.inflate(inflater)
        binding?.run {
            model=mediaPlayerModel
            nowPlaying?.run {
                title.isSelected=true
                playPause.setOnClickListener(this@MediaPlayerFragment)
            }
            playPause.setOnClickListener(this@MediaPlayerFragment)
            seekbar.setOnSeekBarChangeListener(this@MediaPlayerFragment)
        }
        return  binding?.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailViewModel=createViewModel()
        mediaViewModel=createViewModel()
        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
            episode?.let {
                val media=MediaService.MediaItem(
                    title=episode.title,
                    author=episode.author,
                    thumbnail=episode.thumbnail,
                    url=episode.enclosure?.link,
                    description=episode.description
                )
                mediaViewModel?.setMedia(media)
            }

        })
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
                position.observe(this@MediaPlayerFragment, Observer{ pos->
                    pos?.let { mediaPlayerModel.Elapsed=pos}
                })
            })
            controller.observe(this@MediaPlayerFragment, Observer {
                this@MediaPlayerFragment.controller=it
            })
        }
    }

    override fun onResume() {
        super.onResume()
        mediaViewModel?.connect()
    }

    override fun onStop() {
        super.onStop()
        mediaViewModel?.disconnect()
    }


    override fun onProgressChanged(bar: SeekBar?, position:  Int, fromUser: Boolean) {
        mediaPlayerModel.Elapsed= position
    }

    override fun onStartTrackingTouch(bar: SeekBar) {
        controller?.transportControls?.pause()
    }

    override fun onStopTrackingTouch(bar: SeekBar) {
        controller?.transportControls?.seekTo(bar.progress.toLong())
        controller?.transportControls?.play()
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        now_playing?.fade(1 - (slideOffset))
        //thumbnail?.fade(slideOffset )
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {

    }

    override fun onClick(view: View) {
      when(view.id){
            R.id.play_pause->{
                controller?.playbackState?.let {
                      val controls = controller?.transportControls
                      when (it.state) {
                          PlaybackStateCompat.STATE_PLAYING
                              , PlaybackStateCompat.STATE_BUFFERING -> {
                              controls?.pause()
                          }
                          PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {
                              controls?.play()
                          }
                          else->{}
                      }
                }
            }
      }
    }

}