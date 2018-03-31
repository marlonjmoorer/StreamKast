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
import com.example.marlonmoorer.streamkast.adapters.SectionListener
import com.example.marlonmoorer.streamkast.adapters.SectionModel
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.FeatureViewModel
import kotlinx.android.synthetic.main.fragment_featured.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeaturedFragment : Fragment(),SectionListener {



    override fun onShowMore(genre: MediaGenre?)
    {

        fragmentManager?.let{
            it.beginTransaction()
            .setCustomAnimations(
                    R.anim.enter_right,
                    R.anim.exit_right,
                    R.anim.enter_right,
                    R.anim.exit_right)
            .add(android.R.id.content,ListDialogFragment())
            .addToBackStack("list")
            .commit()
        }
        viewModel.loadMore(genre)
    }

    override fun onSelectItem(item: MediaItem) {
        fragmentManager?.let{
            it.beginTransaction()
                    .setCustomAnimations(
                            R.anim.design_bottom_sheet_slide_in,
                            R.anim.design_bottom_sheet_slide_out,
                            R.anim.design_bottom_sheet_slide_in,
                            R.anim.design_bottom_sheet_slide_out)
                    .addToBackStack("list")
                    .add(android.R.id.content,DetailFragment())
                    .commit()
        }
        detailModel.selectShow(item)

    }

    lateinit var adapter: SectionListAdapter
    lateinit var viewModel:FeatureViewModel
    lateinit var detailModel: DetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_featured,
                container, false)
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(FeatureViewModel::class.java!!)
        detailModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java!!)
        adapter= SectionListAdapter()
        viewModel.getFeatured()?.observe(this, Observer{ podcast->
            podcast?.let {
                adapter.prependSection(SectionModel(it,title = "Featured",listener = this) )
            }
        })
        MediaGenre.values().forEach { genre->
            viewModel.getShowsByGenre(genre).observe(this, Observer{ podcast->
                podcast?.let {
                    adapter.addSection(SectionModel(podcast,genre,listener = this) )
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
        contentList.adapter=adapter

    }


}