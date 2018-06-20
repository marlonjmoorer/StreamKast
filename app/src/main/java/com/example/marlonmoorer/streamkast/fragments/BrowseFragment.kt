package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.example.marlonmoorer.streamkast.adapters.CategoryAdapter
import com.example.marlonmoorer.streamkast.adapters.FeaturedPodcastAdapter

import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import kotlinx.android.synthetic.main.fragment_browse.view.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class BrowseFragment : Fragment(){

    lateinit var browseViewModel: BrowseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_browse, container, false)
        val featuredPodcastAdapter=FeaturedPodcastAdapter(browseViewModel)
        view?.apply {
            featured.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            categories.layoutManager = GridLayoutManager(activity, 2)
            categories.adapter = CategoryAdapter(browseViewModel)
            categories.setNestedScrollingEnabled(false)
            featured.adapter = featuredPodcastAdapter
        }

        browseViewModel.getFeaturedByGenre(MediaGenre.Featured.id).observe(this, Observer { podcast ->
            podcast?.let {
               featuredPodcastAdapter.setPodcasts(it)
            }
        })
        activity?.actionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
            title = "Browse"
        }

        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        browseViewModel = createViewModel()
    }


}


