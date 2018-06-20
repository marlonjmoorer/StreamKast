package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.databinding.FragmentMiniPlayerBinding
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import org.jetbrains.anko.support.v4.startActivity



class MiniPlayerFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMiniPlayerBinding.inflate(inflater)
        val intent= Intent(activity,MediaService::class.java)
        activity?.bindService(intent,serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
        return  binding?.root
    }
    private var detailViewModel: DetailViewModel?=null
    private var episodeModel: EpisodeModel?=null
    private var binding:FragmentMiniPlayerBinding?=null
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                episodeModel=binder.getModel()
                binding?.model=episodeModel
                binding?.executePendingBindings()
                binding?.miniTitle?.isSelected=true
                binding?.root?.setOnClickListener {
                    startActivity<MediaPlayerActivity>()
                }
            }
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            episodeModel=null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        detailViewModel=createViewModel()
        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
            episode?.let {
                this.show()
                episodeModel?.setEpisode(episode)
                episodeModel?.prepare()
            }
            binding?.playPause?.setOnClickListener {
               episodeModel?.play_pause()
            }
        })
    }
    fun hide(){
        fragmentManager?.beginTransaction()?.hide(this)?.commit()
    }
    fun show(){
        fragmentManager?.beginTransaction()?.show(this)?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unbindService(serviceConnection)
    }

}