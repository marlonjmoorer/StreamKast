package com.example.marlonmoorer.streamkast

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_media_player.*
import android.databinding.DataBindingUtil
import android.support.v4.media.session.MediaControllerCompat
import android.widget.SeekBar

import android.arch.lifecycle.Observer
import android.support.v4.media.session.PlaybackStateCompat

import android.support.v4.media.MediaMetadataCompat
import android.os.SystemClock
import com.example.marlonmoorer.streamkast.databinding.ActivityMediaPlayerBinding
import com.example.marlonmoorer.streamkast.viewModels.MediaViewModel


class MediaPlayerActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {

    private lateinit var binding: ActivityMediaPlayerBinding
    private  var controller: MediaControllerCompat?=null
    private  var mediaViewModel:MediaViewModel?=null
    private  val mediaPlayerModel=MediaPlayerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_media_player)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }
        seekBar.setOnSeekBarChangeListener(this)
        mediaViewModel= createViewModel()
        mediaViewModel?.apply{
            position.observe(this@MediaPlayerActivity, Observer{ pos->
              pos?.let { mediaPlayerModel.Elapsed=pos}
            })
            metadata.observe(this@MediaPlayerActivity, Observer { data->
                data?.let {
                    with(mediaPlayerModel){
                        Title=it.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
                        Image=it.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
                        Duration=it.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
                    }
                }
            })
            playState.observe(this@MediaPlayerActivity, Observer { state->
                when(state) {
                    PlaybackStateCompat.STATE_PLAYING -> {
                        play.setBackgroundResource(R.drawable.icons8_pause)
                    }
                    PlaybackStateCompat.STATE_PAUSED,
                    PlaybackStateCompat.STATE_NONE,
                    PlaybackStateCompat.STATE_STOPPED,
                    PlaybackStateCompat.STATE_BUFFERING-> {
                        play.setBackgroundResource(R.drawable.icons8_play)
                    }
                }
            })
            controller.observe(this@MediaPlayerActivity, Observer {
                this@MediaPlayerActivity.controller=it
            })
        }
        play.setOnClickListener {
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
        binding.model= mediaPlayerModel

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

}
