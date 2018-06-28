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
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.databinding.FragmentMediaplayerBinding

import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.MediaViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_mediaplayer.*


class MediaPlayerFragment:Fragment(), SlidingUpPanelLayout.PanelSlideListener{


    private var detailViewModel: DetailViewModel?=null
    var mediaViewModel:MediaViewModel?=null
    var mediaPlayerModel=MediaPlayerModel()
    private  var controller: MediaControllerCompat?=null
    private var binding:FragmentMediaplayerBinding?=null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMediaplayerBinding.inflate(inflater)
        binding?.model=mediaPlayerModel
        binding?.nowPlaying?.miniTitle?.isSelected=true
        binding?.playPause?.setOnClickListener {
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
        return  binding?.root
    }

    override fun onResume() {
        super.onResume()
        //mediaViewModel?.connect()
    }

    override fun onStop() {
        super.onStop()
        //mediaViewModel?.disconnect()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hideFragment()
        detailViewModel=createViewModel()
        mediaViewModel=createViewModel()
        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
            episode?.let {
                this.showFragment()

                val media=MediaService.MediaItem(
                    title=episode.title,
                    author=episode.author,
                    thumbnail=episode.thumbnail,
                    url=episode.enclosure?.link,
                    description=episode.description
                )
                mediaViewModel?.setMedia(media)
                mediaViewModel?.connect()
            }

        })
        with(mediaViewModel!!){
            metadata.observe(this@MediaPlayerFragment, Observer { data->
                data?.let {
                    mediaPlayerModel.Image=data.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
                    mediaPlayerModel.Title=data.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
                }
            })
            playState.observe(this@MediaPlayerFragment, Observer { state->
                when(state) {
                    PlaybackStateCompat.STATE_PLAYING -> {
                       play_pause.setBackgroundResource(R.drawable.icons8_pause)
                    }
                    PlaybackStateCompat.STATE_PAUSED,
                    PlaybackStateCompat.STATE_NONE,
                    PlaybackStateCompat.STATE_STOPPED,
                    PlaybackStateCompat.STATE_BUFFERING-> {
                        play_pause.setBackgroundResource(R.drawable.icons8_play)
                    }
                }
            })
            controller.observe(this@MediaPlayerFragment, Observer {
                this@MediaPlayerFragment.controller=it
            })
        }
    }

    fun hideFragment(){
        fragmentManager?.beginTransaction()?.hide(this)?.commit()
    }
    fun showFragment(){
        fragmentManager?.beginTransaction()?.show(this)?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
       // activity?.unbindService(serviceConnection)
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        now_playing?.fade(1 - (slideOffset))
        thumbnail?.fade(slideOffset )
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
       when(newState){

       }
    }

}