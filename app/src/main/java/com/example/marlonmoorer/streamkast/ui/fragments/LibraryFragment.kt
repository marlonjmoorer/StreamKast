package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.DownloadListAdapter
import com.example.marlonmoorer.streamkast.adapters.HistoryListAdapter
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.ui.activities.DeleteActivity
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel
import kotlinx.android.synthetic.main.fragment_library.view.*
import org.jetbrains.anko.support.v4.startActivity


class LibraryFragment:Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_library,container, false).apply {
            viewPager.adapter= ViewPagerAdapter(childFragmentManager)
            tabs.setupWithViewPager(viewPager)
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(view.toolbar)
        }
        setHasOptionsMenu(true)
        return  view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.library_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.action_downloads->{
                startActivity<DeleteActivity>()
                return true
            }
        }
        return false
    }

    class PlaybackHistoryFragment:Fragment(){
        private lateinit var episodeAdapter:HistoryListAdapter
        private  var listener: FragmentEvenListener?=null


        override fun onAttach(context: Context?) {
            super.onAttach(context)
            listener= context as FragmentEvenListener
        }
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            episodeAdapter.clickEvent.subscribe{
                listener?.viewEpisode(it)
            }
            val viewModel=createViewModel<LibraryViewModel>()
            viewModel.getPlayBackHistory().observe(this, Observer { episodes->
                episodes?.let { episodeAdapter.setEpisodes(it) }
            })
        }
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            episodeAdapter= HistoryListAdapter()
            return RecyclerView(context).apply{
                adapter=episodeAdapter
                layoutManager= LinearLayoutManager(context)
            }
        }
    }

    class DownloadListFragment:Fragment(){
        private lateinit var episodeAdapter:DownloadListAdapter
        private  var listener: FragmentEvenListener?=null


        override fun onAttach(context: Context?) {
            super.onAttach(context)
            listener= context as FragmentEvenListener
        }
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val viewModel=createViewModel<LibraryViewModel>()
            viewModel.getDownloads().observe(this, Observer {downloads->
                downloads?.let { episodeAdapter.setEpisodes(it) }
            })

            episodeAdapter.clickEvent.subscribe{
                listener?.viewEpisode(it)
            }
            episodeAdapter.deleteEvent.subscribe {
                viewModel.removeDownload(it.downloadId!!)
            }

        }
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            episodeAdapter= DownloadListAdapter()

            return RecyclerView(context).apply{
                layoutManager= LinearLayoutManager(context)
                adapter=episodeAdapter
            }
        }

    }

    class ViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {
        private var fragments: Map<String, Fragment>
        init {
            fragments= mapOf(
                    "History" to PlaybackHistoryFragment(),
                    "Downloaded" to DownloadListFragment())
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

