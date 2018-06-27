package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.databinding.FragmentMiniPlayerBinding
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.MediaViewModel
import kotlinx.android.synthetic.main.fragment_mini_player.*

import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startActivity


class MiniPlayerFragment:Fragment() {


    private var detailViewModel: DetailViewModel?=null
    var mediaViewModel:MediaViewModel?=null
    var mediaPlayerModel=MediaPlayerModel()
    private  var controller: MediaControllerCompat?=null
    private var binding:FragmentMiniPlayerBinding?=null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMiniPlayerBinding.inflate(inflater)
        binding?.model=mediaPlayerModel
        binding?.root?.setOnClickListener {
            startActivity<MediaPlayerActivity>()
        }
        binding?.miniTitle?.isSelected=true
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
            metadata.observe(this@MiniPlayerFragment, Observer {data->
                data?.let {
                    mediaPlayerModel.Image=data.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
                    mediaPlayerModel.Title=data.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
                }
            })
            playState.observe(this@MiniPlayerFragment, Observer { state->
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
            controller.observe(this@MiniPlayerFragment, Observer {
                this@MiniPlayerFragment.controller=it
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

}