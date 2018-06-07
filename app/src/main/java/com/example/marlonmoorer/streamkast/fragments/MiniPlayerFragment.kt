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
import com.example.marlonmoorer.streamkast.MediaService
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel


class MiniPlayerFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout._mini_player,container,false)
    }
    private var detailViewModel: DetailViewModel?=null
    private var service:MediaService?=null
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                service=binder.getService()
                service?.currentEpisode?.observe(this@MiniPlayerFragment, Observer { e->
                    service?.start()
                })
            }
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            service=null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        detailViewModel=createViewModel()
        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
            episode?.let {
               this.show()
            }
            service?.setPlayList(listOf(episode!!))
        })

        val intent= Intent(activity,MediaService::class.java)
        activity?.bindService(intent,serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }
    fun hide(){
        fragmentManager?.beginTransaction()?.hide(this)?.commit()
    }
    fun show(){
        fragmentManager?.beginTransaction()?.show(this)?.commit()
    }
}