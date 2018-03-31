package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.EpisodeListAdapter
import com.example.marlonmoorer.streamkast.databinding.FragmentDetailsBinding
import com.example.marlonmoorer.streamkast.load
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel

/**
 * Created by marlonmoorer on 3/24/18.
 */
class DetailFragment: Fragment() {


    lateinit var detailModel: DetailViewModel
    lateinit var binding:FragmentDetailsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentDetailsBinding.inflate(inflater,container,false)

        detailModel.selectedPodcast?.observe(this, Observer { podcast->
            binding.channel=podcast
            podcast?.image?.url?.let { binding.mainBackdrop.load(it) }
        })
        detailModel.getEpisodes.observe(this, Observer { episodes->
            var adapter= EpisodeListAdapter(episodes!!)
            binding.episodes.apply {
                layoutManager=LinearLayoutManager(this@DetailFragment.context)
                setAdapter(adapter)
            }

        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detailModel.selectedPodcast.removeObservers(this)
        detailModel.getEpisodes.removeObservers(this)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java!!)

    }


}