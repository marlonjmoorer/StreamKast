package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.createViewModel

import com.example.marlonmoorer.streamkast.listeners.OnPodcastClickListener
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment:Fragment(),OnPodcastClickListener{

    var searchViewModel:SearchViewModel?=null
    var browseViewModel:BrowseViewModel?=null
    var adapter:PodcastListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel=createViewModel()
        browseViewModel=createViewModel()
        adapter=PodcastListAdapter(this)
        searchViewModel?.searchResults?.observe(this@SearchFragment, Observer {results->
            results?.results?.let { adapter?.setPostcasts(it) }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_search,container,false)
        view.apply {
            resultList?.adapter=adapter
            resultList.layoutManager= LinearLayoutManager(activity)
            search_view.setOnQueryChangeListener { oldQuery, newQuery ->
                searchViewModel?.getSearchResults(newQuery)
            }
        }
        return view
    }

    override fun onClick(podcastId: String) {
        this.browseViewModel?.setPodcast(podcastId)
        view?.search_view?.clearSearchFocus()


    }
}