package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.EpisodeListAdapter
import com.example.marlonmoorer.streamkast.adapters.HistoryListAdapter
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel
import kotlinx.android.synthetic.main.fragment_library.view.*
import org.jetbrains.anko.backgroundColor

class LibraryFragment:Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_library,container, false).apply {
            viewPager.adapter= ViewPagerAdapter(childFragmentManager)
            tabs.setupWithViewPager(viewPager)
        }
        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel=createViewModel<LibraryViewModel>()
        viewModel.getPlayBackHistory().observe(this, Observer { itens->
            Log.d("","")
        })
    }

    class PlaybackHistoryFragment:Fragment(){
        private lateinit var episodeAdapter:HistoryListAdapter
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val viewModel=createViewModel<LibraryViewModel>()
            viewModel.getPlayBackHistory().observe(this, Observer { episodes->
                episodes?.let { episodeAdapter.setEpisodes(it) }
            })
        }
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            episodeAdapter= HistoryListAdapter(activity as IEpisodeListener)
            return RecyclerView(context).apply{
                adapter=episodeAdapter
                layoutManager= LinearLayoutManager(context)

            }
        }
    }

    class ViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {
        private var fragments: Map<String, Fragment>
        init {
            fragments= mapOf(
                    "History" to PlaybackHistoryFragment(),
                    "Downloaded" to BrowseFragment() )
        }
        override fun getItem(position: Int): Fragment {
            return fragments.values.elementAt(position)
        }
        override fun getCount()=fragments.size

        override fun getPageTitle(position: Int): CharSequence? {
            return fragments.keys.elementAt(position)
        }
    }

}

