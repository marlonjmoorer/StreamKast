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
import com.example.marlonmoorer.streamkast.fade
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_mediaplayer.*


class EpisodeFragment: BottomSheetDialogFragment() {


    lateinit var episode: IEpisode
    lateinit var binding:FragmentEpisodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episode=it.getSerializable(KEY) as IEpisode
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEpisodeBinding.inflate(inflater)
        binding.handler=activity as IEpisodeListener
        binding.episode=episode
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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