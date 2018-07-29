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
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.ui.adapters.DownloadListAdapter
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.android.synthetic.main.fragment_library.view.*


interface IModeChangeListener{
    fun onModeChange(inEditMode:Boolean)
}

class LibraryFragment:Fragment(),IModeChangeListener{

    private lateinit var adapter: LibraryFragment.ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        adapter=ViewPagerAdapter(childFragmentManager)
        val view= inflater.inflate(R.layout.fragment_library,container, false).apply {
            viewPager.adapter=adapter
            tabs.setupWithViewPager(viewPager)
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(view.toolbar)
        }
        //setHasOptionsMenu(true)
        return  view
    }


    override fun onModeChange(inEditMode: Boolean) {
        if(inEditMode){
            tabs.visibility=View.GONE
        }else{
            tabs.visibility=View.VISIBLE
        }
        viewPager.setOnTouchListener { v, event -> inEditMode }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.library_menu,menu)
    }
    val currentFragment
        get() = adapter.getFragment(viewPager.currentItem)

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_edit->{
                (currentFragment as ActionMode.Callback?)?.let{
                    activity?.startActionMode(it)
                }
            }
        }
        return false
    }


    class DownloadListFragment:Fragment(),ActionMode.Callback{
        private lateinit var episodeAdapter:DownloadListAdapter
        private  var listener: FragmentEvenListener?=null

        override fun onAttach(context: Context?) {
            super.onAttach(context)
            listener= context as FragmentEvenListener
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val viewModel=createViewModel<LibraryViewModel>()
            viewModel.downloadChangeEvent
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        episodeAdapter.update(it)
                    }

            viewModel.getDownloaded().observe(this, Observer {downloads->
                downloads?.let {episodeAdapter.setEpisodes(it) }
            })

            episodeAdapter.clickEvent.subscribe{
                listener?.viewEpisode(it)
            }
            episodeAdapter.deleteEvent.subscribe {
                viewModel.removeDownload(it.guid)
            }


        }
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            episodeAdapter= DownloadListAdapter()
            setHasOptionsMenu(true)
            return RecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = episodeAdapter
            }
        }

       override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when(item?.itemId){
                R.id.action_delete-> episodeAdapter.commitDeletion()
            }
            mode?.finish()
            return true
       }

       override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
           mode?.menuInflater?.inflate(R.menu.edit_menu,menu)
           return true
       }

       override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
           (parentFragment as  IModeChangeListener).onModeChange(true)
           episodeAdapter.setEditeMode(true)
           return true
       }

       override fun onDestroyActionMode(mode: ActionMode?) {
           (parentFragment as  IModeChangeListener).onModeChange(false)
           episodeAdapter.setEditeMode(false)
       }
   }

    inner class ViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {
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

        fun getFragment(index:Int)=getItem(index)


    }




}

