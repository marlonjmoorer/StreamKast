package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.FeaturedPodcastAdapter
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import kotlinx.android.synthetic.main.fragment_section.view.*


private const val KEY = "SECTION_NAME"


class SectionFragment : Fragment() {

    private var genre: MediaGenre? = null
    lateinit var viewModel: BrowseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genre=MediaGenre.parse(it.getString(KEY))
        }
        viewModel.getFeaturedByGenre(genre!!.id,20)?.observe(this,featuredObserver)
        viewModel.getPodcastByGenre(genre!!).observe(this,podcastObserver)

    }

    val featuredObserver= Observer<List<PodcastEntry>?> { podcasts->
        podcasts?.let {
            view?.apply {
                featured_items.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                featured_items.adapter=FeaturedPodcastAdapter(podcasts).apply {
                    handler =viewModel.handler
                }
            }
        }
    }
    val podcastObserver= Observer<List<MediaItem>> { podcasts->
        view?.apply {
            section_items.layoutManager= LinearLayoutManager(activity)
            section_items.adapter=PodcastListAdapter(podcasts!!).apply {
                handler=viewModel.handler
            }
            section_items.setNestedScrollingEnabled(false);
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_section, container, false)
        setHasOptionsMenu(true)
        activity?.actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title=genre?.displayname
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(BrowseViewModel::class.java!!)
    }



    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu);
    }

    companion object {
        @JvmStatic
        fun newInstance(genreId:String) =
                SectionFragment().apply {
                    arguments = Bundle().apply {
                        putString(KEY,genreId)
                    }
                }
    }
}
