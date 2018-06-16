package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.FeaturedPodcastAdapter
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.data.Featured
import com.example.marlonmoorer.streamkast.listeners.OnPodcastClickListener
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel

import kotlinx.android.synthetic.main.fragment_section.view.*


private const val KEY = "SECTION_NAME"


class SectionFragment : Fragment(),OnPodcastClickListener {

    private var title:String?=null
    lateinit var viewModel: BrowseViewModel
    private var podcastAdapter:PodcastListAdapter?=null
    private var featuredPodcastAdapter:FeaturedPodcastAdapter?=null

    override fun onClick(podcastId: String) {
        viewModel.setPodcast(podcastId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        podcastAdapter= PodcastListAdapter(this)
        featuredPodcastAdapter= FeaturedPodcastAdapter(this)
        arguments?.let {
            MediaGenre.parse(it.getString(KEY))?.let { genre->
                viewModel.getFeaturedByGenre(genre.id).observe(this,featuredObserver)
                viewModel.getPodcastByGenre(genre).observe(this,podcastObserver)
                this.title=genre.displayname
            }
        }
    }

    val featuredObserver= Observer<List<Featured>?> { podcasts->
        podcasts?.let {
          featuredPodcastAdapter?.setPodcasts(podcasts)
        }
    }
    val podcastObserver= Observer<List<MediaItem>> { podcasts->
       podcasts?.let {
           podcastAdapter?.setPostcasts(podcasts)
       }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_section, container, false)
        view.apply {
            section_items.apply {
                layoutManager= LinearLayoutManager(activity)
                adapter=podcastAdapter
                setNestedScrollingEnabled(false);
            }
            featured_items.apply {
                layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                adapter=featuredPodcastAdapter
            }
        }

        activity?.actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title=this@SectionFragment.title
        }
        setHasOptionsMenu(true)
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = createViewModel()
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
