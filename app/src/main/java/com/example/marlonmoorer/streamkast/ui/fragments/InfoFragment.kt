package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentInfoBinding
import com.example.marlonmoorer.streamkast.ui.viewModels.DetailViewModel


class InfoFragment: Fragment(){


    private lateinit var binding: FragmentInfoBinding
    private lateinit var detailModel: DetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailModel = createViewModel()
        detailModel.podcastDetails.observe(this, Observer { podcast->
            binding.channel=podcast
            binding.executePendingBindings()
        })
    }

}