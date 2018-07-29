package com.example.marlonmoorer.streamkast.ui.fragments


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager

import com.example.marlonmoorer.streamkast.ui.adapters.CategoryAdapter
import com.example.marlonmoorer.streamkast.ui.adapters.FeaturedPodcastAdapter

import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.ui.viewModels.BrowseViewModel

import kotlinx.android.synthetic.main.fragment_browse.view.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class BrowseFragment : Fragment() {

    lateinit var browseViewModel: BrowseViewModel
    lateinit var featuredPodcastAdapter:FeaturedPodcastAdapter
    lateinit var categoryAdapter: CategoryAdapter
    private  var listener:FragmentEvenListener?=null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener= context as FragmentEvenListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_browse, container, false)
        featuredPodcastAdapter=FeaturedPodcastAdapter()
        categoryAdapter = CategoryAdapter()
        view?.apply {
            featured.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            featured.adapter = featuredPodcastAdapter
            categories.layoutManager = GridLayoutManager(activity, 2)
            categories.adapter =categoryAdapter
            categories.setNestedScrollingEnabled(false)
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        browseViewModel = createViewModel()
        browseViewModel.setGenre(MediaGenre.Featured.id)
        browseViewModel.featuredPodcast.observe(this, Observer { podcast ->
            featuredPodcastAdapter.setPodcasts(podcast?: emptyList())
        })
        featuredPodcastAdapter.clickEvent.subscribe {
            listener?.viewPodcast(it.podcastId)
        }
        categoryAdapter.clickEvent.subscribe {
            listener?.selectGenre(it)
        }
    }
}


