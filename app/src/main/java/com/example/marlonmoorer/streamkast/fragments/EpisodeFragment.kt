package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.Episode

import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel


class EpisodeFragment: BottomSheetDialogFragment(),IEpisodeListener {


    lateinit var detailModel: DetailViewModel
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = createViewModel()
    }

    override fun open(episode: Episode) {
        this.dismiss()
        detailModel.queuedEpisode.postValue(episode)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEpisodeBinding.inflate(inflater)
        detailModel.getCurrentEpisode().observe(this, Observer { episode ->
            binding.episode=episode
        })
        binding.handler=this
        return  binding.root
    }

}