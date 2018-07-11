package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import com.example.marlonmoorer.streamkast.createViewModel

import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment:BaseFragment(){

    var searchViewModel:SearchViewModel?=null
    var browseViewModel:BrowseViewModel?=null
    var adapter:PodcastListAdapter? = null
    private  val queryKey="KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel=createViewModel()
        browseViewModel=createViewModel()
        adapter=PodcastListAdapter(podcastListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_search,container,false)
        view.apply {
            resultList?.adapter=adapter
            resultList.layoutManager= LinearLayoutManager(activity)
            search_view.setOnSearchListener(object:FloatingSearchView.OnSearchListener{
                override fun onSearchAction(currentQuery: String) {
                    searchViewModel?.getSearchResults(currentQuery)

                }
                override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {

                }
            })
        }
        searchViewModel?.searchResults?.observe(this,resultsObserver)
        browseViewModel?.getSelectedPodCastId()?.observe(this,selectedObserver)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchViewModel?.searchResults?.removeObserver(resultsObserver)
        browseViewModel?.getSelectedPodCastId()?.removeObserver(selectedObserver)
    }
    private var resultsObserver= Observer<SearchResults> {results->
        results?.results?.let {
            adapter?.setPostcasts(it)
        }
    }

    private var selectedObserver= Observer<String> {
        view?.search_view?.clearSearchFocus()
    }


}