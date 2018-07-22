package com.example.marlonmoorer.streamkast.fragments

import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.Episode

import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding
import com.example.marlonmoorer.streamkast.fade
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.setIcon
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Status
import kotlinx.android.synthetic.main.fragment_mediaplayer.*
import org.jetbrains.anko.support.v4.toast
import java.util.*


class EpisodeFragment: BottomSheetDialogFragment() {


    lateinit var episode: IEpisode
    lateinit var binding:FragmentEpisodeBinding
    private lateinit var libraryViewModel: LibraryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episode=it.getSerializable(KEY) as IEpisode
        }

    }

    var observer=Observer<Download>{
        when(it?.status){
            Status.FAILED->{

            }
            Status.ADDED->{

            }
            Status.DOWNLOADING->{
                binding.actionButtonDownload.apply{
                    text = "${it.progress}% ..."
                    setIcon(0)
                    isEnabled=false
                }
            }
            Status.COMPLETED-> {
                binding.downloadProgress.progress=0
                binding.actionButtonDownload.apply {
                    text="Downloaded"
                    setIcon(R.drawable.icons8_check_mark_symbol)
                }
            }
            else->{
                print("")
            }
        }

    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEpisodeBinding.inflate(inflater)
        binding.handler=activity as IEpisodeListener
        binding.episode=episode
        binding.actionButtonDownload.setOnClickListener {
            binding.downloadProgress.max=100
            libraryViewModel.queDownload(episode).observe(this,observer)
        }
        return  binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        libraryViewModel=createViewModel()
        libraryViewModel.findExistingDownload(episode.guid).observe(this,observer)
    }
    companion object {
        private const val KEY = "EPISODE"
        @JvmStatic
        fun newInstance(episode: IEpisode) =
               EpisodeFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(KEY,episode)
                    }
                }
    }

}