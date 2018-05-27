package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.marlonmoorer.streamkast.ISelectHandler
import com.example.marlonmoorer.streamkast.adapters.CategoryAdapter
import com.example.marlonmoorer.streamkast.adapters.FeaturedPodcastAdapter
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import kotlinx.android.synthetic.main.fragment_browse.view.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class BrowseFragment : Fragment() {


    lateinit var browseViewModel: BrowseViewModel




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_browse, container, false)
        view?.apply {
            featured.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            categories.layoutManager = GridLayoutManager(activity, 2)
            val adapter = CategoryAdapter()
            adapter.handler = browseViewModel.handler
            categories.adapter = adapter
            categories.setNestedScrollingEnabled(false);
        }

        browseViewModel.getFeaturedByGenre(BrowseViewModel.FEATURED)?.observe(this@BrowseFragment, Observer { podcast ->
            podcast?.let {
                val adapter = FeaturedPodcastAdapter(podcast)
                adapter.handler = browseViewModel.handler
                view.featured.adapter = adapter
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
        browseViewModel = ViewModelProviders.of(activity!!).get(BrowseViewModel::class.java!!)
    }


}


