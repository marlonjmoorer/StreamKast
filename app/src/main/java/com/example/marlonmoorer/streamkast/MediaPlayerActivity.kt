package com.example.marlonmoorer.streamkast

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_media_player.*
import org.jetbrains.anko.support.v4.startActivity
import android.databinding.DataBindingUtil
import android.os.Handler
import android.support.v4.media.session.MediaControllerCompat
import android.view.KeyEvent
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.databinding.ActivityMediaPlayerBinding
import org.jetbrains.anko.appcompat.v7.contentFrameLayout
import org.jetbrains.anko.runOnUiThread
import java.text.FieldPosition
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MediaPlayerActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {

    private var episodeModel: EpisodeModel?=null

    private lateinit var binding: ActivityMediaPlayerBinding
    val timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_media_player)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }
        val intent= Intent(this,MediaService::class.java)
        bindService(intent,serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        timer.cancel()
    }
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                episodeModel=binder.getModel().apply {
                    listener=this@MediaPlayerActivity
                }
                binding.model=episodeModel
                binding.seekBar.max=episodeModel?.length?:0
                binding.play.setOnClickListener {
                    episodeModel?.play_pause()
                }
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                    title=episodeModel?.title
                }
                MediaControllerCompat.setMediaController(this@MediaPlayerActivity,binder.controller)
                startSeekBarTracking()
            }
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            episodeModel=null
        }
    }
    fun startSeekBarTracking(){
      timer.scheduleAtFixedRate(object :TimerTask(){
          override fun run() {
              if(episodeModel!!.isPlaying){
                  updateUi()
              }
          }
      },0,1000)
    }

    fun updateUi()=runOnUiThread{
        binding.seekBar.progress= episodeModel?.position?:0
        episodeModel?.notifyPropertyChanged(BR.elapsed)
    }


    override fun onProgressChanged(bar: SeekBar?, position:  Int, fromUser: Boolean) {
        if (fromUser){
            episodeModel?.seekTo(position)
            binding.elapsed.text= position.toTime()
        }
    }

    override fun onStartTrackingTouch(bar: SeekBar) {
        episodeModel?.pause()
    }

    override fun onStopTrackingTouch(bar: SeekBar?) {
        episodeModel?.play()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

}
