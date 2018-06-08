package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R

import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.github.magneticflux.rss.namespaces.standard.elements.Item

class EpisodeFragment: BottomSheetDialogFragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEpisodeBinding.inflate(inflater)
        binding.handler=createViewModel<DetailViewModel>().apply {
            queuedEpisode.observe(this@EpisodeFragment, Observer {episode->
                this@EpisodeFragment.dismiss()
            })
            binding.episode=currentEpisode
        }
        return  binding.root
    }

}