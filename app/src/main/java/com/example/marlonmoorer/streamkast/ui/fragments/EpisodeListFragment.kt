package com.example.marlonmoorer.streamkast.ui.fragments


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.ui.adapters.EpisodeListAdapter
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.ui.viewModels.DetailViewModel


class EpisodeListFragment: Fragment(){

    lateinit var detailModel: DetailViewModel
    lateinit var episodeListAdapter: EpisodeListAdapter
    private  var listener: FragmentEvenListener?=null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener= context as FragmentEvenListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        episodeListAdapter= EpisodeListAdapter()
        return RecyclerView(context).apply{
            layoutManager= LinearLayoutManager(context)
            adapter= episodeListAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailModel = createViewModel()
        detailModel.episodes.observe(this, Observer { episodes->
            episodeListAdapter.setEpisodes(episodes?: emptyList())
        })
        episodeListAdapter.run {
            openEvent.subscribe{
            listener?.viewEpisode(it)
            }
            playEvent.subscribe {
                listener?.playEpisode(it)
            }
        }

    }
}