package com.example.marlonmoorer.streamkast.ui.fragments

import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R

import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding

import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.setIcon
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel



class EpisodeFragment: BottomSheetDialogFragment() {


    lateinit var episode: IEpisode
    lateinit var binding:FragmentEpisodeBinding
    private lateinit var libraryViewModel: LibraryViewModel
    private  var listener: FragmentEvenListener?=null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener= context as FragmentEvenListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episode=it.getSerializable(KEY) as IEpisode
        }

    }


    var observer=Observer<LibraryViewModel.DownloadInfo>{
        when(it?.status){
            DownloadManager.STATUS_PENDING->{
                binding.actionButtonDownload.apply{
                    text = "Pending ..."
                    setIcon(0)
                    isEnabled=false
                }
            }
            DownloadManager.STATUS_RUNNING->{
                binding.actionButtonDownload.apply{
                    text = "Downloading (${it.progress}%) ..."
                    setIcon(0)
                    isEnabled=false
                }
            }
            DownloadManager.STATUS_SUCCESSFUL->{
                binding.downloadProgress.progress=0
                binding.actionButtonDownload.apply {
                    text="Downloaded"
                    setIcon(R.drawable.icons8_check_mark_symbol)
                }
            }
        }

    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEpisodeBinding.inflate(inflater)
        binding.episode=episode
        binding.actionButtonPlay.setOnClickListener {
            listener?.playEpisode(episode)
        }
        binding.actionButtonDownload.setOnClickListener {
            binding.downloadProgress.max=100
            libraryViewModel.queDownload(episode).observe(this,observer)
        }
        return  binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        libraryViewModel=createViewModel()
        libraryViewModel.findPreviousDownload(episode.guid).observe(this,observer)
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