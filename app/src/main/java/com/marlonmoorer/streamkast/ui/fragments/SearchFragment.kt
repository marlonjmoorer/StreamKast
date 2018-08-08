package com.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.Context
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.marlonmoorer.streamkast.R
import com.marlonmoorer.streamkast.SearchState
import com.marlonmoorer.streamkast.ui.adapters.PagedPodcastListAdapter
import com.marlonmoorer.streamkast.api.models.Podcast
import com.marlonmoorer.streamkast.createViewModel
import com.marlonmoorer.streamkast.databinding.FragmentSearchBinding
import com.marlonmoorer.streamkast.ui.activities.FragmentEvenListener

import com.marlonmoorer.streamkast.ui.viewModels.BrowseViewModel
import com.marlonmoorer.streamkast.ui.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment: BaseFragment(),PagedPodcastListAdapter.PodcastListCallBack{

    private lateinit var searchViewModel:SearchViewModel
    private lateinit var browseViewModel:BrowseViewModel
    private lateinit var adapter:PagedPodcastListAdapter
    private  lateinit var binding:FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=FragmentSearchBinding.inflate(inflater,container,false)
        adapter=PagedPodcastListAdapter(this)
        binding.apply {
            resultList.adapter=adapter
            resultList.layoutManager= LinearLayoutManager(activity)
            searchView.setOnSearchListener(object:FloatingSearchView.OnSearchListener{
                override fun onSearchAction(currentQuery: String) {
                    if(!currentQuery.isEmpty()){
                     searchViewModel.searchForPodcast(currentQuery)
                    }
                }
                override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {

                }
            })
            searchView.setSearchFocused(true)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel=createViewModel()
        browseViewModel=createViewModel()
        searchViewModel.searchResults.observe(this@SearchFragment,resultsObserver)
    }

    override fun onOpen(id: String) {
        listener?.viewPodcast(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchViewModel.searchResults.removeObserver(resultsObserver)
    }
    private var resultsObserver= Observer<PagedList<Podcast>> {results->

        results?.let {
            adapter.submitList(results)
            adapter.notifyDataSetChanged()
        }
    }
    
}