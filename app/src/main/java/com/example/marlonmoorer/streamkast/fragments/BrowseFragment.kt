package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.example.marlonmoorer.streamkast.adapters.CategoryAdapter
import com.example.marlonmoorer.streamkast.adapters.SectionAdapter
import com.example.marlonmoorer.streamkast.adapters.SectionListAdapter
import com.example.marlonmoorer.streamkast.api.models.Genre
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_browse.view.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class BrowseFragment : Fragment(),View.OnClickListener {

    lateinit var adapter: SectionListAdapter
    lateinit var viewModel: BrowseViewModel
    lateinit var detailModel: DetailViewModel


    override fun onClick(view: View) {
        val item= view.tag
        if(item is MediaGenre){
            val fragment= SectionFragment.newInstance(item.id)
            this.loadFragment(fragment)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_browse, container, false)
        view?.apply {
            featured.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            categories.layoutManager=GridLayoutManager(activity,2)
            val adapter=CategoryAdapter()
            adapter.listener=this@BrowseFragment
            categories.adapter=adapter
            categories.setNestedScrollingEnabled(false);
        }

        viewModel.getSection(BrowseViewModel.FEATURED)?.observe(this@BrowseFragment, Observer{ podcast->
            podcast?.let {
                view.featured.adapter =SectionAdapter(podcast)
            }
        })

        return view
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(BrowseViewModel::class.java!!)
        detailModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java!!)
        adapter= SectionListAdapter(model = viewModel)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()


    }

    fun loadFragment(fragment: Fragment){
        fragmentManager!!.beginTransaction()
                    .setCustomAnimations(
                            R.anim.enter_right,
                            R.anim.exit_right,
                            R.anim.enter_right,
                            R.anim.exit_right)
                    //.remove(it.findFragmentByTag("over"))
                    //.replace(R.id.main,fragment,"over")
                    .replace(R.id.container,fragment)
                    .addToBackStack("over")
                    .commit()
        }
    }


