package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.ui.adapters.FeaturedPodcastAdapter
import com.example.marlonmoorer.streamkast.ui.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.data.Featured
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.ui.viewModels.BrowseViewModel

import kotlinx.android.synthetic.main.fragment_section.view.*




class SectionFragment : Fragment() {
    private  val KEY = "SECTION_NAME"
    lateinit var viewModel: BrowseViewModel
    private var podcastAdapter:PodcastListAdapter?=null
    private var featuredPodcastAdapter:FeaturedPodcastAdapter?=null
    lateinit var  genre: MediaGenre

    private  var listener: FragmentEvenListener?=null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener= context as FragmentEvenListener
    }

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
        podcastAdapter= PodcastListAdapter()
        featuredPodcastAdapter= FeaturedPodcastAdapter()
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
        viewModel.setGenre(genre.id)
        viewModel.featuredPodcast.observe(this,featuredObserver)
        viewModel.latestPodcast.observe(this,podcastObserver)
        podcastAdapter?.openEvent?.subscribe{
            listener?.viewPodcast(it.collectionId)
        }
        podcastAdapter?.toggleSubEvent?.subscribe{
            listener?.toggleSubscription(it)
        }
        featuredPodcastAdapter?.clickEvent?.subscribe{
            listener?.viewPodcast(it.podcastId)
        }
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
