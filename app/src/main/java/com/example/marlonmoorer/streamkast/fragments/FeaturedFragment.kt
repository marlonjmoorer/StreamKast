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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.marlonmoorer.streamkast.adapters.SectionListAdapter
import com.example.marlonmoorer.streamkast.adapters.SectionModel
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.viewModels.FeatureViewModel
import com.example.marlonmoorer.streamkast.viewModels.ListDialogViewModel
import kotlinx.android.synthetic.main.fragment_featured.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeaturedFragment : Fragment() {

    lateinit var adapter: SectionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_featured,
                container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val featureViewModel = ViewModelProviders.of(activity!!).get(FeatureViewModel::class.java!!)
        val listViewModel=ViewModelProviders.of(activity!!).get(ListDialogViewModel::class.java!!)
        var sections= mutableListOf<SectionModel>()
        adapter= SectionListAdapter(sections,listViewModel)
        featureViewModel.getFeatured()?.observe(this, Observer{ podcast->
            podcast?.let {
                adapter.prependSection(SectionModel(it,title = "Featured") )
            }
        })
        MediaGenre.values().forEach { genre->
            featureViewModel.getShowsByGenre(genre).observe(this, Observer{ podcast->
                podcast?.let {
                    adapter.addSection(SectionModel(podcast,genre) )
                }
            })
        }
        listViewModel.isLoading.observe(this,Observer{loading->
           if (loading!!) {
               fragmentManager?.let{
                   it.beginTransaction()
                           .setCustomAnimations(
                                   R.anim.design_bottom_sheet_slide_in,
                                   R.anim.design_bottom_sheet_slide_out)
//                           .setCustomAnimations(
//                                   R.anim.card_flip_right_in,
//                                   R.anim.card_flip_right_out,
//                                   R.anim.card_flip_left_in,
//                                   R.anim.card_flip_left_out)
                           .addToBackStack("list")
                           .add(android.R.id.content,ListDialogFragment())
                           .commit()
                   //  ListDialogFragment().show(fragmentManager,"list")
               }
           }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        contentList.layoutManager= LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        contentList.hasFixedSize()
        contentList.adapter=adapter

    }


}