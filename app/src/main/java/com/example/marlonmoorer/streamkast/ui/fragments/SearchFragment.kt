package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.Context
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
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener

import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment: Fragment(){

    private lateinit var searchViewModel:SearchViewModel
    private lateinit var browseViewModel:BrowseViewModel
    private lateinit var adapter:PagedPodcastListAdapter

    private  var listener: FragmentEvenListener?=null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener= context as FragmentEvenListener
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_search,container,false)
        adapter=PagedPodcastListAdapter()
        view.apply {
            resultList?.adapter=adapter
            resultList.layoutManager= LinearLayoutManager(activity)
            search_view.setOnSearchListener(object:FloatingSearchView.OnSearchListener{
                override fun onSearchAction(currentQuery: String) {
                    searchViewModel.searchForPodcast(currentQuery)
                }
                override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {

                }
            })
            search_view.setSearchFocused(true)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel=createViewModel()
        browseViewModel=createViewModel()
        searchViewModel.searchResults.observe(this@SearchFragment,resultsObserver)
        adapter.openEvent.subscribe{
            listener?.viewPodcast(it.collectionId)
        }
        adapter.toggleSubEvent.subscribe{
            listener?.toggleSubscription(it)
        }
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