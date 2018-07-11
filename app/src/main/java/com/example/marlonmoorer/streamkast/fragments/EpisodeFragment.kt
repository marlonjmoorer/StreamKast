package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.rss.Episode

import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding
import com.example.marlonmoorer.streamkast.fade
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_mediaplayer.*


class EpisodeFragment: BottomSheetDialogFragment() {


    lateinit var detailModel: DetailViewModel
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = createViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEpisodeBinding.inflate(inflater)
        detailModel.getCurrentEpisode().observe(this, Observer { episode ->
            binding.episode=episode
        })
        binding.handler=activity as IEpisodeListener
        return  binding.root
    }

}