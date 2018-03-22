package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import com.example.marlonmoorer.streamkast.adapters.SectionAdapter
import com.example.marlonmoorer.streamkast.viewModels.FeatureViewModel
import kotlinx.android.synthetic.main.fragment_featured.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeaturedFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_featured,
                container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        contentList.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        contentList.hasFixedSize()
        val model = ViewModelProviders.of(this).get(FeatureViewModel::class.java!!)
        model.getFeatured()?.observe(this, Observer{ podcast->
            if(contentList.adapter==null){
                contentList.adapter= podcast?.let { SectionAdapter(it) }
            }

        })
    }
}