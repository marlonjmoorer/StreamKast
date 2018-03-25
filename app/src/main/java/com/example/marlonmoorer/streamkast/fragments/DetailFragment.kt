package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel

/**
 * Created by marlonmoorer on 3/24/18.
 */
class DetailFragment: Fragment() {


    lateinit var detailModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_main,container,false)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java!!)
        detailModel.selectedPodcast?.observe(this, Observer { podcast->

        })
        detailModel.getEpisodes.observe(this, Observer { episodes->


        })
    }


}