package com.example.marlonmoorer.streamkast.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.marlonmoorer.streamkast.R
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


    inner class ViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {
        private var fragments: Map<String, Fragment>
        init {
            fragments= mapOf(
                    getString(R.string.label_history) to PlaybackHistoryFragment(),
                    getString(R.string.label_downloads) to DownloadListFragment())
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

