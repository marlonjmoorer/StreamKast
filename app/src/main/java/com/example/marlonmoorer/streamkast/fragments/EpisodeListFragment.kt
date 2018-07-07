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
import com.example.marlonmoorer.streamkast.adapters.EpisodeListAdapter
import com.example.marlonmoorer.streamkast.api.models.Episode
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel


class EpisodeListFragment: Fragment(), IEpisodeListener {


    lateinit var detailModel: DetailViewModel

    override fun open(episode: Episode) {
        detailModel.setEpisode(episode)
    }

    override fun play(episode: Episode) {
        detailModel.queuedEpisode.postValue(episode)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = createViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val episodeListAdapter= EpisodeListAdapter(this)
        detailModel.getEpisodes().observe(this, Observer { episodes->
            episodes?.let {
                episodeListAdapter.setEpisodes(it)
            }
        })
        return RecyclerView(activity).apply {
            adapter=episodeListAdapter
            layoutManager= LinearLayoutManager(context)
        }
    }
}