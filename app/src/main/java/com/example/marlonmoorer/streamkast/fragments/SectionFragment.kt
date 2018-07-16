package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.FeaturedPodcastAdapter
import com.example.marlonmoorer.streamkast.adapters.PagedPodcastListAdapter
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.data.Featured
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel

import kotlinx.android.synthetic.main.fragment_section.view.*




class SectionFragment : BaseFragment() {
    private  val KEY = "SECTION_NAME"
    lateinit var viewModel: BrowseViewModel
    private var podcastAdapter:PodcastListAdapter?=null
    private var featuredPodcastAdapter:FeaturedPodcastAdapter?=null
    lateinit var  genre: MediaGenre



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            MediaGenre.parse(it.getString(KEY))?.let { genre->
                this.genre=genre
            }
        }
        setHasOptionsMenu(true)
    }

    val featuredObserver= Observer<List<Featured>?> { podcasts->
        podcasts?.let {
          featuredPodcastAdapter?.setPodcasts(podcasts)
        }
    }
    val podcastObserver= Observer<List<Podcast>> { podcasts->
       podcasts?.let {
           podcastAdapter?.setPostcasts(podcasts)
       }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_section, container, false)
        podcastAdapter= PodcastListAdapter(podcastListener)
        featuredPodcastAdapter= FeaturedPodcastAdapter(podcastListener)
        view.apply {
            section_items.apply {
                layoutManager= LinearLayoutManager(activity)
                adapter=podcastAdapter
                setNestedScrollingEnabled(false)
            }
            featured_items.apply {
                layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                adapter=featuredPodcastAdapter
            }
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(view.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                title=genre.displayname
            }
            view.toolbar.setNavigationOnClickListener {
                this.onBackPressed()
            }
        }

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = createViewModel()
        viewModel.getFeaturedByGenre(genre.id).observe(this,featuredObserver)
        viewModel.getPodcastByGenre(genre.id).observe(this,podcastObserver)
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
