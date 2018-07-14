package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.PagedPodcastListAdapter
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import com.example.marlonmoorer.streamkast.createViewModel

import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment:BaseFragment(){

    var searchViewModel:SearchViewModel?=null
    var browseViewModel:BrowseViewModel?=null
    var adapter:PagedPodcastListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel=createViewModel()
        browseViewModel=createViewModel()
        adapter=PagedPodcastListAdapter(podcastListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_search,container,false)
        view.apply {
            resultList?.adapter=adapter
            resultList.layoutManager= LinearLayoutManager(activity)
            search_view.setOnSearchListener(object:FloatingSearchView.OnSearchListener{
                override fun onSearchAction(currentQuery: String) {
                    searchViewModel?.getPagesSearchResults(currentQuery)?.observe(this@SearchFragment,resultsObserver)
                }
                override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {

                }
            })
            search_view.setSearchFocused(true)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchViewModel?.searchResults?.removeObserver(resultsObserver)
    }
    private var resultsObserver= Observer<PagedList<Podcast>> {results->
        results?.let {
            adapter?.submitList(results)
            adapter?.notifyDataSetChanged()
        }
    }




}