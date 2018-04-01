package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.example.marlonmoorer.streamkast.R
import kotlinx.android.synthetic.main.fragment_list_dialog.*
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.viewModels.SectionViewModel


/**
 * Created by marlonmoorer on 3/22/18.
 */
class ListDialogFragment: Fragment() {

    lateinit var adapter: PodcastListAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val model = ViewModelProviders.of(activity!!).get(SectionViewModel::class.java!!)
        adapter= PodcastListAdapter(listOf(),model)
        model.podcasts?.observe(this, Observer { podcast->
                adapter= podcast?.let { PodcastListAdapter(it,model) }!!
                selection.swapAdapter(adapter,true)
        })
        model.isLoading.observe(this, Observer { loading->
            loading_screen.visibility=  if (loading!!) View.VISIBLE else View.GONE
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater?.inflate(R.layout.fragment_list_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        selection.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            setItemViewCacheSize(30);
            setDrawingCacheEnabled(true);
            adapter =this@ListDialogFragment.adapter
        }
        tool_bar.setNavigationOnClickListener{
            fragmentManager?.popBackStack()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}