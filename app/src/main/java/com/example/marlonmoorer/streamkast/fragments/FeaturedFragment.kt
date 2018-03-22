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
import com.example.marlonmoorer.streamkast.adapters.SectionAdapter
import com.example.marlonmoorer.streamkast.adapters.SectionListAdpater
import com.example.marlonmoorer.streamkast.adapters.SectionModel
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.viewModels.FeatureViewModel
import kotlinx.android.synthetic.main.fragment_featured.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeaturedFragment : Fragment() {

    lateinit var adapter:SectionListAdpater

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_featured,
                container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val model = ViewModelProviders.of(this).get(FeatureViewModel::class.java!!)
        var sections= mutableListOf<SectionModel>()
        adapter= SectionListAdpater(sections,context!!)
        model.getFeatured()?.observe(this, Observer{ podcast->
            podcast?.let {
                adapter.prependSection(SectionModel("Featured", it) )
            }
        })
        MediaGenre.values().take(4).forEach { genre->
            model.getShowsByGenre(genre).observe(this, Observer{ podcast->
                podcast?.let {
                    adapter.addSection(SectionModel(genre.displayname(), podcast) )
                }
            })
        }
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