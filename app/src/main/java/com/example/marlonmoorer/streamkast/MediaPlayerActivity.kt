package com.example.marlonmoorer.streamkast

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_media_player.*
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.util.Log
import android.widget.SeekBar
import com.example.marlonmoorer.streamkast.databinding.ActivityMediaPlayerBinding


class MediaPlayerActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {

    private var mediaModel: MediaModel?=null

    private lateinit var binding: ActivityMediaPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_media_player)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }
        val intent= Intent(this,MediaService::class.java)
        bindService(intent,serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)

    }
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                mediaModel=binder.getMediaModel()
                binding.model=mediaModel
                binding.seekBar.apply {
                    max=mediaModel?.length?:0
                    setOnSeekBarChangeListener(this@MediaPlayerActivity)
                }
                binding.play.setOnClickListener {
                    mediaModel?.togglePlayback()
                }
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                    title=mediaModel?.title
                }
                mediaModel?.addOnPropertyChangedCallback(propertyChangedCallback)
            }
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            mediaModel?.removeOnPropertyChangedCallback(propertyChangedCallback)
            mediaModel=null
        }
    }



    override fun onProgressChanged(bar: SeekBar?, position:  Int, fromUser: Boolean) {
        if (fromUser){
            mediaModel?.seekTo(position)
            binding.elapsed.text= position.toTime()
        }
    }

    override fun onStartTrackingTouch(bar: SeekBar) {
        mediaModel?.pause()
    }

    override fun onStopTrackingTouch(bar: SeekBar?) {
        mediaModel?.play()
    }

    private val propertyChangedCallback=object :Observable.OnPropertyChangedCallback(){
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
          if(propertyId==BR.elapsed){
              seekBar.progress= mediaModel?.position!!
          }
        }
    }




}
