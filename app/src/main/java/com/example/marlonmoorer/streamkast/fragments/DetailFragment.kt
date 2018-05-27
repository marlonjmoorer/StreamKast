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
import com.example.marlonmoorer.streamkast.adapters.EpisodeListAdapter
import com.example.marlonmoorer.streamkast.databinding.FragmentDetailsBinding
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel

/**
 * Created by marlonmoorer on 3/24/18.
 */
class DetailFragment: Fragment() {


    lateinit var detailModel: DetailViewModel
    lateinit var binding:FragmentDetailsBinding
    private  var Id:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Id=it.getString(KEY)
        }


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailsBinding.inflate(inflater,container,false)
        detailModel.channel.observe(this, Observer { channel->
            var adapter= EpisodeListAdapter(channel?.episodes!!)
            binding.episodes.apply {
                layoutManager=LinearLayoutManager(this@DetailFragment.context)
                setAdapter(adapter)
            }
            binding.channel=channel
        })

        detailModel.loadPodcast(Id).observe(this, Observer {podcast->
            binding.podcast=podcast

        })

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        detailModel.selectedPodcast.removeObservers(this)
        detailModel.episodes.removeObservers(this)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java!!)

    }
    companion object {
        private const val KEY = "PODCAST_ID"
        @JvmStatic
        fun newInstance(podcastId:String) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(KEY,podcastId)
                    }
                }
    }


}