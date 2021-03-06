package com.marlonmoorer.streamkast.ui.fragments


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marlonmoorer.streamkast.ui.adapters.EpisodeListAdapter
import com.marlonmoorer.streamkast.createViewModel
import com.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.marlonmoorer.streamkast.ui.viewModels.DetailViewModel


class EpisodeListFragment: BaseFragment(){

    lateinit var detailModel: DetailViewModel
    lateinit var episodeListAdapter: EpisodeListAdapter

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