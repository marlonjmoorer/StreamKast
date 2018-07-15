package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethanhua.skeleton.Skeleton
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.EpisodeListAdapter
import com.example.marlonmoorer.streamkast.api.models.rss.Episode
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel


class EpisodeListFragment: BaseFragment(){


    lateinit var detailModel: DetailViewModel
    lateinit var episodeList:RecyclerView

    lateinit var episodeListAdapter: EpisodeListAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = createViewModel()
        episodeListAdapter= EpisodeListAdapter(episodeListener)
        episodeList=RecyclerView(context).apply{
            layoutManager= LinearLayoutManager(context)
            adapter= episodeListAdapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        detailModel.episodes.observe(this, Observer { episodes->
            episodeListAdapter.setEpisodes(episodes?: emptyList())
        })
        return episodeList
    }
}