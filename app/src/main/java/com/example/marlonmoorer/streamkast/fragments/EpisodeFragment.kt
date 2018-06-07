package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.Episode
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel

class EpisodeFragment: BottomSheetDialogFragment() {

    lateinit var episode:Episode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          episode= it.getSerializable(KEY) as Episode
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentEpisodeBinding.inflate(inflater)
        binding.episode=episode
        binding.handler=createViewModel<DetailViewModel>().apply {
            queuedEpisode?.observe(this@EpisodeFragment, Observer {episode->
                this@EpisodeFragment.dismiss()
            })
        }
        return  binding.root
    }
    companion object {
        private const val KEY = "EPISODE"
        @JvmStatic
        fun newInstance(episode: Episode) =
              EpisodeFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(KEY,episode)
                    }
                }
    }
}