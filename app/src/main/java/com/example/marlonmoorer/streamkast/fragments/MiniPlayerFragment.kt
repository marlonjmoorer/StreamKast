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
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startActivity


class MiniPlayerFragment:Fragment() {


    private var detailViewModel: DetailViewModel?=null
    private var mediaModel: MediaModel?=null
    private var binding:FragmentMiniPlayerBinding?=null
    private  var serviceBound:Boolean=false

    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                mediaModel=binder.getMediaModel()
                binding?.model=mediaModel
                binding?.miniTitle?.isSelected=true
                binding?.root?.setOnClickListener {
                    startActivity<MediaPlayerActivity>()
                }
                binding?.executePendingBindings()
                serviceBound=true
            }
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            mediaModel=null
            serviceBound=false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMiniPlayerBinding.inflate(inflater)
        return  binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hideFragment()
        detailViewModel=createViewModel()
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

                if(!serviceBound) {
                    val intent = intentFor<MediaService>()
                    intent.putExtra(MediaService.MEDIA,media)
                    activity?.startService(intent)
                    activity?.bindService(intent, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
                }else{
                    val broadcastIntent = Intent(MediaService.PLAY_AUDIO)
                    broadcastIntent.putExtra(MediaService.MEDIA,media)
                    activity?.sendBroadcast(broadcastIntent)
                }
            }
            binding?.playPause?.setOnClickListener {
               mediaModel?.togglePlayback()
            }
        })
    }
    fun hideFragment(){
        fragmentManager?.beginTransaction()?.hide(this)?.commit()
    }
    fun showFragment(){
        fragmentManager?.beginTransaction()?.show(this)?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unbindService(serviceConnection)
    }

}